package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.convert.AccountBalanceConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Subject;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.AccountBalanceMapper;
import org.finance.business.mapper.AccountCloseListMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.service.event.AccountEvent;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    private AccountCloseListMapper accountCloseListMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onAccountClosed(YearMonth yearMonth) {
        List<Subject> subjects = subjectMapper.listOrderByTree();
        Map<Long, AccountBalance> summaryResult = summaryBySubjectId(yearMonth, subjects, voucherItemMapper::summaryMonthGroupBySubjectAndCurrency);
        if (summaryResult.isEmpty()) {
            return;
        }
        List<AccountBalance> balances = new ArrayList<>(summaryResult.values());
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

    public Map<Long, AccountBalance> summaryBySubjectId(YearMonth yearMonth, List<Subject> subjects,
                                                        Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        return summaryByYearMonth(yearMonth, subjects, voucherItemsByYearMonth);
    }

    public Map<Long, AccountBalance> summaryBySubjectNumber(YearMonth yearMonth, List<Subject> subjects,
                                                           Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        return summaryByYearMonth(yearMonth, subjects, voucherItemsByYearMonth);
    }

    public Map<Long, AccountBalance> summaryBySubject(YearMonth yearMonth, List<Subject> subjects,
                                                            Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        return summaryByYearMonth(yearMonth, subjects, voucherItemsByYearMonth);
    }

    /**
     * 汇总 yearMonth时期的 科目余额
     *
     * @param subjects 默认为null，查询所有科目，否则查询指定科目
     */
    private Map<Long, AccountBalance> summaryByYearMonth(YearMonth yearMonth,
                                                               List<Subject> subjects,
                                                               Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        int yearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth));

        // 1. 根据科目分组查询统计 YearMonth时期的 借贷总和
        Map<Long, AccountBalance> balanceOfCurrentBySubjectId = listAccountBalanceByYearMonth(yearMonth, subjects, voucherItemsByYearMonth);

        if (balanceOfCurrentBySubjectId.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, List<Subject>> subjectsByLevel = subjects.stream().collect(Collectors.groupingBy(Subject::getLevel));

        // 3. 获取树形科目最大深度
        Integer maxLevel = subjectsByLevel.keySet().stream().max(Integer::compareTo).get();

        // 4. 从科目最底层，一级一级往上合计相关余额信息
        for (int i = maxLevel - 1; i >= 1; i--) {
            List<Subject> subjectByLevel = subjectsByLevel.get(i);
            if (subjectByLevel == null) {
                continue;
            }
            for (Subject subject : subjectByLevel) {
                List<Subject> childrenSubject = subjectsByLevel.get(i + 1).stream()
                        .filter(child -> subject.getId().equals(child.getParentId())
                                && balanceOfCurrentBySubjectId.containsKey(child.getId())
                        )
                        .collect(Collectors.toList());
                if (childrenSubject.isEmpty()) {
                    continue;
                }
                AccountBalance balance = summaryChildrenAccountBalance(balanceOfCurrentBySubjectId, childrenSubject,
                        () -> AccountBalance.newInstance(subject).setYear(yearMonth.getYear()).setYearMonthNum(yearMonthNum)
                );
                balanceOfCurrentBySubjectId.put(subject.getId(), balance);
            }
        }
        Map<Long, Subject> subById = subjects.stream().collect(Collectors.toMap(Subject::getId, v -> v));
        balanceOfCurrentBySubjectId.values().forEach(accountBalance -> {
            accountBalance.setSubject(subById.get(accountBalance.getSubjectId()));
        });
        return balanceOfCurrentBySubjectId;
    }

    private Map<Long, AccountBalance> listAccountBalanceByYearMonth(YearMonth yearMonth, List<Subject> subjects,
                                                                    Function<Integer, List<VoucherItem>> voucherItemsByYearMonth) {
        int yearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth));
        // 如果当期已经结账从AccountBalance里取
        if (accountCloseListMapper.alreadyAccountClose(yearMonthNum)) {
            return baseMapper.listByYearMonth(yearMonthNum).stream()
                    .collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));
        }
        // 否则从凭证里面取
        Map<Long, AccountBalance> balanceBySubjectId = voucherItemsByYearMonth.apply(yearMonthNum).stream()
                .map(AccountBalanceConvert.INSTANCE::toAccountBalance)
                .collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));
        Customer proxyCustomer = SecurityUtil.getProxyCustomer();
        Integer enablePeriod = proxyCustomer.getEnablePeriod();
        // 判断当月是否是初始余额月份，如果从初始余额中累加统计年累计金额，否则从上月结账数据中累加
        boolean isInitialBalanceDate = yearMonthNum == enablePeriod;
        List<AccountBalance> accountBalances;
        if (isInitialBalanceDate) {
            accountBalances = subjects.stream().map(AccountBalanceConvert.INSTANCE::toAccountBalance).collect(Collectors.toList());
        } else {
            // 2. 获取上一个月的科目余额
            YearMonth lastYearMonth = yearMonth.minusMonths(1);
            int lastYearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(lastYearMonth));
            accountBalances = baseMapper.listByYearMonth(lastYearMonthNum);
        }
        return accountBalances.stream()
                .map(accountBalance -> {
                    Long subjectId = accountBalance.getSubjectId();
                    AccountBalance balanceOfVoucher = balanceBySubjectId.get(subjectId);
                    if (balanceOfVoucher == null) {
                        return accountBalance;
                    }
                    return balanceOfVoucher.setOpeningBalance(accountBalance.getOpeningBalance().add(balanceOfVoucher.getOpeningBalance()))
                            .setClosingBalance(accountBalance.getClosingBalance().add(balanceOfVoucher.getClosingBalance()))
                            .setLocalOpeningBalance(accountBalance.getLocalOpeningBalance().add(balanceOfVoucher.getLocalOpeningBalance()))
                            .setLocalClosingBalance(accountBalance.getLocalClosingBalance().add(balanceOfVoucher.getLocalClosingBalance()))
                            .setDebitAnnualAmount(accountBalance.getDebitAnnualAmount().add(balanceOfVoucher.getDebitAnnualAmount()))
                            .setCreditAnnualAmount(accountBalance.getCreditAnnualAmount().add(balanceOfVoucher.getCreditAnnualAmount()));
                })
                .collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));
    }

    /**
     * 合计childrenSubject科目的余额
     */
    private AccountBalance summaryChildrenAccountBalance(Map<Long, AccountBalance> balancesBySubject,
                                                               List<Subject> childrenSubject, Supplier<AccountBalance> newInstance) {
        List<AccountBalance> balanceByCurrency = childrenSubject.stream()
                .filter(child -> balancesBySubject.containsKey(child.getId()))
                .map(child -> balancesBySubject.get(child.getId()))
                .collect(Collectors.toList());

        return   AccountBalanceConvert.INSTANCE.sumAccountBalances(balanceByCurrency, newInstance);
    }

}
