package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.convert.AccountBalanceConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.Subject;
import org.finance.business.mapper.AccountBalanceMapper;
import org.finance.business.mapper.InitialBalanceItemMapper;
import org.finance.business.mapper.InitialBalanceMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.service.event.AccountEvent;
import org.finance.infrastructure.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private InitialBalanceMapper initialBalanceMapper;
    @Resource
    private InitialBalanceItemMapper initialBalanceItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onAccountClosed(YearMonth yearMonth) {
        Collection<AccountBalance> summaryResult = summary(yearMonth);
        if (summaryResult.isEmpty()) {
            return;
        }
        this.saveBatch(summaryResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onCancelClosedAccount(YearMonth yearMonth) {
        int yearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth));
        baseMapper.delete(Wrappers.<AccountBalance>lambdaQuery()
                .eq(AccountBalance::getYearMonthNum, yearMonthNum)
        );
    }

    public Collection<AccountBalance> summary(YearMonth yearMonth) {
        InitialBalance initialBalance = initialBalanceMapper.selectOne(null);
        return summaryByYearMonth(yearMonth, null, initialBalance);
    }

    /**
     * 汇总 yearMonth时期的 科目余额
     *
     * @param subjects 默认为null，查询所有科目，否则查询指定科目
     */
    public Collection<AccountBalance> summaryByYearMonth(YearMonth yearMonth, List<Subject> subjects, InitialBalance initialBalance) {
        int yearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth));

        // 根据科目分组查询统计 YearMonth时期的 借贷总和
        Map<Long, AccountBalance> balanceOfCurrentBySubjectId = listAccountBalanceOfCurrent(yearMonthNum, initialBalance);
        if (balanceOfCurrentBySubjectId.isEmpty()) {
            // yearMonth 若是没有凭证，就没必要统计科目余额了
            return Collections.emptyList();
        }

        List<Subject> dbSubjects = subjects;
        if (dbSubjects == null) {
            // 查询所有科目，用于统计科目余额
            dbSubjects = subjectMapper.selectList(Wrappers.<Subject>lambdaQuery()
                    .orderByAsc(Subject::getRootNumber, Subject::getLeftValue)
            );
        }
        // 获取上一个月的科目余额
        Map<Long, AccountBalance> balanceOfLastPeriodBySubjectId = listAccountBalanceOfLastPeriod(yearMonth, dbSubjects, initialBalance)
                .stream().collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));

        Map<Integer, List<Subject>> subjectsByLevel = dbSubjects.stream().collect(Collectors.groupingBy(Subject::getLevel));

        // 获取树形科目最大深度
        Integer maxLevel = subjectsByLevel.keySet().stream().max(Integer::compareTo).get();

        // 从科目最底层，一级一级往上合计相关余额信息
        for (int i = maxLevel; i >= 1; i--) {
            List<Subject> subjectByLevel = subjectsByLevel.get(i);
            for (Subject subject : subjectByLevel) {
                AccountBalance balanceOfLastPeriod = balanceOfLastPeriodBySubjectId.get(subject.getId());
                // 最底层科目余额 ==> 查询相关凭证记录进行合计
                if (!subject.getHasLeaf()) {
                    AccountBalance accountBalance = Optional.ofNullable(balanceOfCurrentBySubjectId.get(subject.getId()))
                            .orElseGet(AccountBalance::newInstance)
                            .setYear(yearMonth.getYear()).setYearMonthNum(yearMonthNum)
                            .setSubjectId(subject.getId()).setSubjectNumber(subject.getNumber());
                    accountBalance.mergeLastPeriod(balanceOfLastPeriod);
                    balanceOfCurrentBySubjectId.put(subject.getId(), accountBalance);
                    continue;
                }

                // 非最底层科目余额 ==> 直接合计已经计算好的子级科目余额
                List<Subject> childrenSubject = subjectsByLevel.get(i + 1).stream()
                        .filter(child -> subject.getId().equals(child.getParentId()))
                        .collect(Collectors.toList());
                AccountBalance accountBalance = summaryChildrenAccountBalance(balanceOfCurrentBySubjectId, childrenSubject)
                        .setYear(yearMonth.getYear()).setYearMonthNum(yearMonthNum)
                        .setSubjectId(subject.getId()).setSubjectNumber(subject.getNumber());
                balanceOfCurrentBySubjectId.put(subject.getId(), accountBalance);
            }
        }
        if (subjects == null) {
            return dbSubjects.stream().map(sub -> balanceOfCurrentBySubjectId.get(sub.getId())).collect(Collectors.toList());
        }
        return balanceOfCurrentBySubjectId.values();
    }

    private Map<Long, AccountBalance> listAccountBalanceOfCurrent(int yearMonthNum, InitialBalance initialBalance) {
        List<AccountBalance> accountBalances = baseMapper.selectList(Wrappers.<AccountBalance>lambdaQuery().eq(AccountBalance::getYearMonthNum, yearMonthNum));
        if (!accountBalances.isEmpty()) {
            return accountBalances.stream().collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));
        }

        // 如果科目余额表没有数据，考虑可能是当月没有关账
        // 先判断当月是否在初始余额表前一个月(8月份的期末余额=9月份的初始余额)
        boolean equalsInitialBalanceDate = initialBalance != null
                && yearMonthNum / 100 == initialBalance.getYear()
                && yearMonthNum == initialBalance.getYearMonthNum() - 1;
        if (equalsInitialBalanceDate) {
            return initialBalanceItemMapper.summaryGroupBySubjectId(initialBalance.getId())
                    .stream().map(AccountBalanceConvert.INSTANCE::toAccountBalance)
                    .collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));
        }

        return voucherItemMapper.summaryGroupBySubjectId(yearMonthNum)
                .stream().map(AccountBalanceConvert.INSTANCE::toAccountBalance)
                .collect(Collectors.toMap(AccountBalance::getSubjectId, v -> v));
    }


    /**
     * 查询yearMonth上一个月的科目余额
     */
    private Collection<AccountBalance> listAccountBalanceOfLastPeriod(YearMonth yearMonth, List<Subject> dbSubjects, InitialBalance initialBalance) {
        YearMonth lastYearMonth = YearMonth.from(yearMonth).minusMonths(1);
        int lastYearMonthNum = Integer.parseInt(Constants.YEAR_MONTH_FMT.format(lastYearMonth));
        // 先查询科目余额表
        List<AccountBalance> accountBalanceOfLastPeriod = baseMapper.summaryGroupBySubjectId(lastYearMonthNum);

        if (!accountBalanceOfLastPeriod.isEmpty()) {
            return accountBalanceOfLastPeriod;
        }

        if (isAfterInitialBalanceDate(yearMonth, initialBalance)) {
            return summaryByYearMonth(lastYearMonth, dbSubjects, initialBalance);
        }
        return accountBalanceOfLastPeriod;
    }

    /**
     * yearMonth是否在初始余额日期之后（包含）
     */
    private boolean isAfterInitialBalanceDate(YearMonth yearMonth, InitialBalance initialBalance) {
        if (initialBalance == null) {
            return false;
        }
        YearMonth initialBalanceDate = YearMonth.parse(initialBalance.getYearMonthNum().toString(), Constants.YEAR_MONTH_FMT);
        return yearMonth.equals(initialBalanceDate) || yearMonth.isAfter(initialBalanceDate);
    }

    /**
     * 合计childrenSubject科目的余额
     */
    private AccountBalance summaryChildrenAccountBalance(Map<Long, AccountBalance> accountBalanceBySubjectId, List<Subject> childrenSubject) {
        BigDecimal zero = new BigDecimal("0");
        BigDecimal debitCurrentBalance = zero, creditCurrentBalance = zero,
                debitOpeningBalance = zero, creditOpeningBalance = zero,
                debitClosingBalance = zero, creditClosingBalance = zero,
                debitAnnualAmount = zero, creditAnnualAmount = zero;

        for (Subject child : childrenSubject) {
            AccountBalance childAccountBalance = accountBalanceBySubjectId.get(child.getId());
            if (childAccountBalance == null) {
                continue;
            }
            debitCurrentBalance = debitCurrentBalance.add(childAccountBalance.getDebitCurrentAmount());
            creditCurrentBalance = creditCurrentBalance.add(childAccountBalance.getCreditCurrentAmount());
            debitOpeningBalance = debitOpeningBalance.add(childAccountBalance.getDebitOpeningAmount());
            creditOpeningBalance = creditOpeningBalance.add(childAccountBalance.getCreditOpeningAmount());
            debitClosingBalance = debitClosingBalance.add(childAccountBalance.getDebitClosingAmount());
            creditClosingBalance = creditClosingBalance.add(childAccountBalance.getCreditClosingAmount());
            debitAnnualAmount = debitAnnualAmount.add(childAccountBalance.getDebitAnnualAmount());
            creditAnnualAmount = creditAnnualAmount.add(childAccountBalance.getCreditAnnualAmount());
        }
        return AccountBalance.newInstance()
                .setDebitCurrentAmount(debitCurrentBalance)
                .setCreditCurrentAmount(creditCurrentBalance)
                .calcOpeningBalance(debitOpeningBalance, creditOpeningBalance)
                .calcClosingBalance(debitClosingBalance, creditClosingBalance)
                .setDebitAnnualAmount(debitAnnualAmount)
                .setCreditAnnualAmount(creditAnnualAmount);
    }
}
