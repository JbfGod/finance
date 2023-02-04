package org.finance.business.manage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.AccountBalanceConvert;
import org.finance.business.convert.ReportConvert;
import org.finance.business.convert.VoucherConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.Subject;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.param.QueryVoucherItemOfSubLegerParam;
import org.finance.business.service.AccountBalanceService;
import org.finance.business.service.SubjectService;
import org.finance.business.service.VoucherItemService;
import org.finance.business.web.request.QueryDailyBankRequest;
import org.finance.business.web.request.QueryDailyCashRequest;
import org.finance.business.web.request.QuerySubLedgerRequest;
import org.finance.business.web.vo.AccountBalanceVO;
import org.finance.business.web.vo.DailyBankVO;
import org.finance.business.web.vo.DailyCashVO;
import org.finance.business.web.vo.GeneralLedgerVO;
import org.finance.business.web.vo.SubLedgerVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Service
public class ReportManage {

    @Resource
    private AccountBalanceService accountBalanceService;
    @Resource
    private SubjectService subjectService;
    @Resource
    private VoucherItemService voucherItemService;

    public List<AccountBalanceVO> listAccountBalance(YearMonth yearMonth) {
        Function<Long, String> nameBySubjectId = subjectService.getNameFunction();
        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
                .orderByAsc(Subject::getRootNumber, Subject::getNumber)
        );
        Map<Long, AccountBalance> accountBalanceBySubjectId = accountBalanceService.summaryBySubjectId(yearMonth, subjects, voucherItemService::summaryByMonthGroupBySubject);
        return subjects.stream().map(sub -> {
            AccountBalance accountBalance = Optional.ofNullable(accountBalanceBySubjectId.get(sub.getId()))
                    .orElseGet(() -> AccountBalance.newInstance(sub));
            AccountBalanceVO accountBalanceVO = AccountBalanceConvert.INSTANCE.toAccountBalance(accountBalance);
            accountBalanceVO.setSubjectName(nameBySubjectId.apply(accountBalance.getSubjectId()));
            return accountBalanceVO;
        }).collect(Collectors.toList());
    }

    public List<GeneralLedgerVO> listGeneralLedger(YearMonth startMonth, YearMonth endMonth, String currencyName) {
        Map<Long, List<GeneralLedgerVO>> result = new LinkedHashMap<>();

        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
                .orderByAsc(Subject::getRootNumber, Subject::getNumber));

        Function<Integer, List<VoucherItem>> summaryByCurrencyAndGroupBySubject = (yearMonth) ->
                voucherItemService.summaryByCurrencyGroupBySubject(yearMonth, currencyName);

        // 筛选所有1级科目
        List<Subject> subjectsOfFirstLevel = subjects.stream().filter(sub -> sub.getLevel() == 1).collect(Collectors.toList());
        YearMonth tmpYearMonth = startMonth;
        // 汇总所有1及科目的余额
        while (tmpYearMonth.isBefore(endMonth) || tmpYearMonth.equals(endMonth)) {
            int monthValue = tmpYearMonth.getMonthValue();
            Map<Long, AccountBalance> accountBalanceBySubjectId = accountBalanceService.summaryBySubjectId(tmpYearMonth, subjects, summaryByCurrencyAndGroupBySubject);
            for (Subject subject : subjectsOfFirstLevel) {
                AccountBalance accountBalance = Optional.ofNullable(accountBalanceBySubjectId.get(subject.getId()))
                        .orElseGet(() -> AccountBalance.newInstance(subject).setYearMonthNum(monthValue));
                List<GeneralLedgerVO> ledgersBySubject = Optional.ofNullable(result.get(subject.getId())).orElseGet(ArrayList::new);
                ledgersBySubject.addAll(ReportConvert.INSTANCE.toGeneralLedgerVO(
                        accountBalance, subject.getName(), ledgersBySubject.isEmpty()
                ));
                result.put(subject.getId(), ledgersBySubject);
            }
            tmpYearMonth = tmpYearMonth.plusMonths(1);
        }

        return result.values().stream()
                .flatMap(ledgers -> {
                    ledgers.get(0).setRowSpan(ledgers.size());
                    return ledgers.stream();
                })
                .collect(Collectors.toList());
    }

    public List<SubLedgerVO> listSubLedger(QuerySubLedgerRequest request) {
        YearMonth startMonth = request.getStartMonth();
        YearMonth endMonth = request.getEndMonth();
        String currencyName = request.getCurrencyName();
        Long subjectId = request.getSubjectId();
        List<SubLedgerVO> result = new ArrayList<>();

        Subject dbSubject = subjectService.getById(subjectId);
        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getParentId, dbSubject.getId())
        );

        Consumer<VoucherItem> forEachVoucherItem = (voucherItem) -> {
            result.add(ReportConvert.INSTANCE.toSubLedgerVO(voucherItem));
        };
        Function<Integer, List<VoucherItem>> summaryByCurrencyAndGroupBySubject = (yearMonth) -> {
            QueryVoucherItemOfSubLegerParam param = new QueryVoucherItemOfSubLegerParam();
            param.setCurrencyName(currencyName);
            param.setYearMonthNum(yearMonth);
            param.setRootNumber(dbSubject.getRootNumber());
            return voucherItemService.summaryVoucherItem(param, forEachVoucherItem);
        };

        YearMonth tmpYearMonth = startMonth;
        AccountBalance openingAccountBalance = null;
        while (tmpYearMonth.isBefore(endMonth) || tmpYearMonth.equals(endMonth)) {
            Map<Long, AccountBalance> accountBalanceBySubjectId = accountBalanceService.summaryBySubjectId(tmpYearMonth, subjects, summaryByCurrencyAndGroupBySubject);
            AccountBalance accountBalance = Optional.ofNullable(accountBalanceBySubjectId.get(subjectId))
                    .orElseGet(AccountBalance::newInstance);
            result.addAll(ReportConvert.INSTANCE.toSubLedgerVO(accountBalance, tmpYearMonth));

            if (tmpYearMonth.equals(startMonth)) {
                openingAccountBalance = accountBalance;
            }
            tmpYearMonth = tmpYearMonth.plusMonths(1);
        }

        AtomicReference<BigDecimal> openingBalance = new AtomicReference<>(openingAccountBalance.getOpeningBalance());

        SubLedgerVO openingSubLedger = new SubLedgerVO()
                .setVoucherDate(startMonth.atDay(1))
                .setSummary("期初余额")
                .setBalance(openingBalance.get());
        result.add(0, openingSubLedger);
        result.stream().filter(ledger -> ledger.getVoucherNumber() != null)
                .forEach(ledger -> ledger.setBalance(
                        openingBalance.updateAndGet(balance ->
                                ledger.getDebitAmount().subtract(ledger.getCreditAmount()).add(balance)
                        )
                ));
        return result;
    }

    public List<DailyCashVO> listDailyCash(QueryDailyCashRequest request) {
        String currencyName = request.getCurrencyName();
        LocalDate voucherDate = request.getVoucherDate();
        Long subjectId = request.getSubjectId();
        // 获取所有现金科目
        List<Subject> cashSubjects = subjectService.listChildren(subjectId);
        if (cashSubjects.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Subject> subjectById = cashSubjects.stream().collect(Collectors.toMap(Subject::getId, v -> v));

        Function<Integer, List<VoucherItem>> fetchAccountBalanceOfYesterday = (yearMonthNum) -> (
                voucherItemService.summaryGroupBySubjectAndCurrency(yearMonthNum,
                        // 由于summary是递归逐月查询的，所以统计voucherDate时候的余额，只有和yearMonth同一个月份的时候才带条件（voucherDate）查询凭证
                        voucherDate.getYear() * 100 + voucherDate.getMonthValue() == yearMonthNum ? voucherDate.minusDays(1) : null,
                        currencyName
                )
        );

        // 查询昨日余额
        Map<Long, AccountBalance> accountBalanceOfYesterday = accountBalanceService.summaryBySubjectId(YearMonth.from(voucherDate), cashSubjects, fetchAccountBalanceOfYesterday);
        // 查询今日借贷金额
        List<DailyCashVO> voucherItems = voucherItemService.summaryDailyGroupBySubjectAndCurrency(voucherDate, currencyName)
                .stream().filter(dailyCash -> subjectById.containsKey(dailyCash.getSubjectId()))
                .map(VoucherConvert.INSTANCE::toDailyCashVO).collect(Collectors.toList());

        List<DailyCashVO> result = new ArrayList<>();
        Map<String, List<DailyCashVO>> dailyCashByKey = voucherItems.stream().collect(Collectors.groupingBy(DailyCashVO::getCurrency));
        dailyCashByKey.forEach((key, dailyCashes) -> {
            result.addAll(ReportConvert.INSTANCE.toDailyCashVOList(dailyCashes, accountBalanceOfYesterday));
        });
        result.stream().filter(dailyCashVO -> dailyCashVO.getSubjectId() != null).forEach(dailyCashVO -> {
            dailyCashVO.setSubjectName(subjectById.get(dailyCashVO.getSubjectId()).getName());
        });
        if (result.isEmpty()) {
            return result;
        }
        result.add(ReportConvert.INSTANCE.toSummaryTotalDailyCashVO(result));
        return result;
    }

    public List<DailyBankVO> listDailyBank(QueryDailyBankRequest request) {
        String currencyName = request.getCurrencyName();
        LocalDate voucherDate = request.getVoucherDate();
        Long subjectId = request.getSubjectId();
        // 获取所有银行科目
        List<Subject> bankSubjects = subjectService.listChildren(subjectId);
        if (bankSubjects.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Subject> subjectById = bankSubjects.stream().collect(Collectors.toMap(Subject::getId, v -> v));

        Function<Integer, List<VoucherItem>> fetchAccountBalanceOfYesterday = (yearMonthNum) -> (
                voucherItemService.summaryGroupBySubjectAndCurrency(yearMonthNum,
                        // 由于summary是递归逐月查询的，所以统计voucherDate时候的余额，只有和yearMonth同一个月份的时候才带条件（voucherDate）查询凭证
                        voucherDate.getYear() * 100 + voucherDate.getMonthValue() == yearMonthNum ? voucherDate.minusDays(1) : null,
                        currencyName
                )
        );

        // 查询昨日余额
        Map<Long, AccountBalance> accountBalanceOfYesterday = accountBalanceService.summaryBySubjectId(YearMonth.from(voucherDate), bankSubjects, fetchAccountBalanceOfYesterday);
        // 查询今日借贷金额
        List<DailyBankVO> voucherItems = voucherItemService.summaryDailyGroupBySubjectAndCurrency(voucherDate, currencyName)
                .stream().filter(dailyBank -> subjectById.containsKey(dailyBank.getSubjectId()))
                .map(VoucherConvert.INSTANCE::toDailyBankVO).collect(Collectors.toList());

        List<DailyBankVO> result = new ArrayList<>();
        Map<String, List<DailyBankVO>> dailyBankByKey = voucherItems.stream().collect(Collectors.groupingBy(DailyBankVO::getCurrency));
        dailyBankByKey.forEach((key, dailyBanks) -> {
            result.addAll(ReportConvert.INSTANCE.toDailyBankVOList(dailyBanks, accountBalanceOfYesterday));
        });
        result.stream().filter(dailyBankVO -> dailyBankVO.getSubjectId() != null).forEach(dailyBankVO -> {
            dailyBankVO.setSubjectName(subjectById.get(dailyBankVO.getSubjectId()).getName());
        });
        if (result.isEmpty()) {
            return result;
        }
        result.add(ReportConvert.INSTANCE.toSummaryTotalDailyBankVO(result));
        return result;
    }

}
