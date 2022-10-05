package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.convert.AccountBalanceConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.Subject;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.AccountBalanceMapper;
import org.finance.business.mapper.InitialBalanceItemMapper;
import org.finance.business.mapper.InitialBalanceMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.service.event.AccountEvent;
import org.finance.infrastructure.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 科目余额 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-16
 */
@Service
public class AccountBalanceService extends ServiceImpl<AccountBalanceMapper, AccountBalance> implements AccountEvent {
    @Resource
    private VoucherItemMapper voucherItemMapper;
    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private InitialBalanceMapper initialBalanceMapper;
    @Resource
    private InitialBalanceItemMapper initialBalanceItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onAccountClosed(YearMonth yearMonth) {
        List<Subject> subjects = subjectMapper.listOrderByTree();
        Map<Long, List<AccountBalance>> summaryResult = summaryGroupBySubject(yearMonth, subjects, voucherItemMapper::summaryMonthGroupBySubjectAndCurrency);
        if (summaryResult.isEmpty()) {
            return;
        }
        List<AccountBalance> balances = summaryResult.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        this.saveBatch(balances);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onCancelClosedAccount(YearMonth yearMonth) {
        int yearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth));
        baseMapper.delete(Wrappers.<AccountBalance>lambdaQuery()
            .eq(AccountBalance::getYearMonthNum, yearMonthNum)
        );
    }

    public Map<Long, List<AccountBalance>> summaryGroupBySubject(YearMonth yearMonth, List<Subject> subjects, Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        InitialBalance initialBalance = initialBalanceMapper.selectOne(null);
        return summaryByYearMonth(yearMonth, subjects, initialBalance, voucherItemsByYearMonth);
    }

    /**
     * 汇总 yearMonth时期的 科目余额
     *
     * @param subjects 默认为null，查询所有科目，否则查询指定科目
     */
    private Map<Long, List<AccountBalance>> summaryByYearMonth(YearMonth yearMonth,
                                                         List<Subject> subjects,
                                                         InitialBalance initialBalance,
                                                         Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        int yearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth));

        // 根据科目分组查询统计 YearMonth时期的 借贷总和
        Map<Long, List<AccountBalance>> balanceOfCurrentBySubjectId = listAccountBalanceByYearMonth(yearMonthNum, initialBalance, voucherItemsByYearMonth);

        // 获取上一个月的科目余额
        YearMonth lastYearMonth = yearMonth.minusMonths(1);
        int lastYearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(lastYearMonth));
        // 先查询科目余额表
        List<AccountBalance> balancesOfLastPeriod = baseMapper.listByYearMonth(lastYearMonthNum);
        Map<Long, List<AccountBalance>> balanceOfLastPeriodBySubjectId;
        if (balancesOfLastPeriod.isEmpty() && isAfterInitialBalanceDate(lastYearMonth, initialBalance)) {
            balanceOfLastPeriodBySubjectId = summaryByYearMonth(lastYearMonth, subjects, initialBalance, voucherItemsByYearMonth);
        } else {
            balanceOfLastPeriodBySubjectId = AccountBalanceConvert.INSTANCE.toGroupBySubject(balancesOfLastPeriod);
        }

        Map<Integer, List<Subject>> subjectsByLevel = subjects.stream().collect(Collectors.groupingBy(Subject::getLevel));

        // 获取树形科目最大深度
        Integer maxLevel = subjectsByLevel.keySet().stream().max(Integer::compareTo).get();

        // 从科目最底层，一级一级往上合计相关余额信息
        for (int i = maxLevel; i >= 1; i--) {
            List<Subject> subjectByLevel = subjectsByLevel.get(i);
            if (subjectByLevel == null) {
                continue;
            }
            for (Subject subject : subjectByLevel) {
                // 最底层科目余额 ==> 查询相关凭证记录进行合计
                if (!subject.getHasLeaf()) {
                    Map<String, AccountBalance> balanceByCurrencyOfLastPeriod = AccountBalanceConvert.INSTANCE.toMapByCurrency(
                        balanceOfLastPeriodBySubjectId.get(subject.getId())
                    );
                    List<AccountBalance> accountBalances = balanceOfCurrentBySubjectId.get(subject.getId());
                    if (CollectionUtils.isEmpty(accountBalances) && CollectionUtils.isEmpty(balanceByCurrencyOfLastPeriod)) {
                        continue;
                    }
                    Map<String, AccountBalance> balanceByCurrency = AccountBalanceConvert.INSTANCE.toMapByCurrency(
                        accountBalances
                    );
                    List<AccountBalance> balances = Stream.of(balanceByCurrencyOfLastPeriod.keySet(), balanceByCurrency.keySet())
                            .flatMap(Collection::stream)
                            .distinct()
                            .map(currency -> (
                                    Optional.ofNullable(balanceByCurrency.get(currency))
                                            .orElseGet(() -> AccountBalance.newInstance(subject))
                                            .setYear(yearMonth.getYear()).setYearMonthNum(yearMonthNum)
                                            .setCurrency(currency)
                                            .mergeLastPeriod(balanceByCurrencyOfLastPeriod.get(currency))
                            )).collect(Collectors.toList());

                    balanceOfCurrentBySubjectId.put(subject.getId(), balances);
                    continue;
                }

                // 非最底层科目余额 ==> 直接合计已经计算好的子级科目余额
                List<Subject> childrenSubject = subjectsByLevel.get(i + 1).stream()
                        .filter(child -> subject.getId().equals(child.getParentId())
                            && balanceOfCurrentBySubjectId.containsKey(child.getId())
                        )
                        .collect(Collectors.toList());
                if (childrenSubject.isEmpty()) {
                    continue;
                }
                List<AccountBalance> balances = summaryChildrenAccountBalance(balanceOfCurrentBySubjectId, childrenSubject,
                    () -> AccountBalance.newInstance(subject).setYear(yearMonth.getYear()).setYearMonthNum(yearMonthNum)
                );
                balanceOfCurrentBySubjectId.put(subject.getId(), balances);
            }
        }
        return balanceOfCurrentBySubjectId;
    }

    private Map<Long, List<AccountBalance>> listAccountBalanceByYearMonth(int yearMonthNum, InitialBalance initialBalance,
                                                                    Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        List<AccountBalance> accountBalances = baseMapper.listByYearMonth(yearMonthNum);
        if (!accountBalances.isEmpty()) {
            return accountBalances.stream().collect(Collectors.groupingBy(AccountBalance::getSubjectId));
        }

        // 如果科目余额表没有数据，考虑可能是当月没有关账
        // 先判断当月是否在初始余额表前一个月(8月份的期末余额=9月份的初始余额)
        boolean equalsInitialBalanceDate = initialBalance != null
                && yearMonthNum / 100 == initialBalance.getYear()
                && yearMonthNum == initialBalance.getYearMonthNum() - 1;
        if (equalsInitialBalanceDate) {
            return initialBalanceItemMapper.summaryGroupBySubject(initialBalance.getId())
                    .stream().map(AccountBalanceConvert.INSTANCE::toAccountBalance)
                    .collect(Collectors.groupingBy(AccountBalance::getSubjectId));
        }

        return voucherItemsByYearMonth.apply(yearMonthNum).stream()
                .map(AccountBalanceConvert.INSTANCE::toAccountBalance)
                .collect(Collectors.groupingBy(AccountBalance::getSubjectId));
    }

    /**
     * 合计childrenSubject科目的余额
     */
    private List<AccountBalance> summaryChildrenAccountBalance(Map<Long, List<AccountBalance>> balancesBySubject,
                                                               List<Subject> childrenSubject, Supplier<AccountBalance> newInstance) {
        Map<String, List<AccountBalance>> balanceByCurrency = childrenSubject.stream()
                .filter(child -> balancesBySubject.containsKey(child.getId()))
                .flatMap(child -> balancesBySubject.get(child.getId()).stream())
                .collect(Collectors.groupingBy(AccountBalance::getCurrency));

        return balanceByCurrency.keySet().stream().map(currency -> (
            AccountBalanceConvert.INSTANCE.sumAccountBalances(balanceByCurrency.get(currency), newInstance)
        )).collect(Collectors.toList());
    }

    /**
     * yearMonth是否在初始余额日期之后（包含）
     */
    private boolean isAfterInitialBalanceDate(YearMonth yearMonth, InitialBalance initialBalance) {
        if (initialBalance == null) {
            return false;
        }
        YearMonth initialBalanceDate = YearMonth.parse(initialBalance.getYearMonthNum().toString(), Constants.YEAR_MONTH_FMT).minusMonths(1);
        return yearMonth.equals(initialBalanceDate) || yearMonth.isAfter(initialBalanceDate);
    }

}
