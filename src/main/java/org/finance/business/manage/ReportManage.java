package org.finance.business.manage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.AccountBalanceConvert;
import org.finance.business.convert.ReportConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.Subject;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.param.QueryVoucherItemOfSubLegerParam;
import org.finance.business.service.AccountBalanceService;
import org.finance.business.service.SubjectService;
import org.finance.business.service.VoucherItemService;
import org.finance.business.web.request.QuerySubLedgerRequest;
import org.finance.business.web.vo.AccountBalanceVO;
import org.finance.business.web.vo.GeneralLedgerVO;
import org.finance.business.web.vo.SubLedgerVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
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
                .orderByAsc(Subject::getRootNumber, Subject::getLeftValue)
        );
        Map<Long, AccountBalance> accountBalanceBySubjectId =
                accountBalanceService.summary(yearMonth, subjects, voucherItemService::summaryGroupBySubjectId);
        return subjects.stream().map(sub -> {
            AccountBalance accountBalance = accountBalanceBySubjectId.get(sub.getId());
            AccountBalanceVO accountBalanceVO = AccountBalanceConvert.INSTANCE.toAccountBalance(accountBalance);
            accountBalanceVO.setSubjectName(nameBySubjectId.apply(accountBalance.getSubjectId()));
            return accountBalanceVO;
        }).collect(Collectors.toList());
    }

    public List<GeneralLedgerVO> listGeneralLedger(YearMonth startMonth, YearMonth endMonth, String currencyName) {
        Map<Long, List<GeneralLedgerVO>> result = new LinkedHashMap<>();

        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery().orderByAsc(Subject::getRootNumber, Subject::getLeftValue));


        Function<Integer, List<VoucherItem>> summaryByCurrencyAndGroupBySubject = (yearMonth) ->
                voucherItemService.summaryByCurrencyGroupBySubjectId(yearMonth, currencyName);

        // 筛选所有1级科目
        List<Subject> subjectsOfFirstLevel = subjects.stream().filter(sub -> sub.getLevel() == 1).collect(Collectors.toList());
        YearMonth tmpYearMonth = startMonth;
        // 汇总所有1及科目的余额
        while (tmpYearMonth.isBefore(endMonth) || tmpYearMonth.equals(endMonth)) {
            int monthValue = tmpYearMonth.getMonthValue();
            Map<Long, AccountBalance> accountBalanceBySubjectId =
                    accountBalanceService.summary(tmpYearMonth, subjects, summaryByCurrencyAndGroupBySubject);
            for (Subject subject : subjectsOfFirstLevel) {
                AccountBalance accountBalance = Optional.ofNullable(accountBalanceBySubjectId.get(subject.getId()))
                        .orElseGet(() -> AccountBalance.newInstance().setYearMonthNum(monthValue)
                                .setSubjectId(subject.getId()).setSubjectNumber(subject.getNumber())
                        );
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

    public List<SubLedgerVO> listSubLedger(@Valid QuerySubLedgerRequest request) {
        YearMonth startMonth = request.getStartMonth();
        YearMonth endMonth = request.getEndMonth();
        String currencyName = request.getCurrencyName();
        Long subjectId = request.getSubjectId();
        List<SubLedgerVO> result = new ArrayList<>();

        Subject dbSubject = subjectService.getById(subjectId);
        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
                .ge(Subject::getLeftValue, dbSubject.getLeftValue())
                .le(Subject::getRightValue, dbSubject.getRightValue())
        );

        Consumer<VoucherItem> forEachVoucherItem = (voucherItem) -> {
            result.add(ReportConvert.INSTANCE.toSubLedgerVO(voucherItem));
        };
        Function<Integer, List<VoucherItem>> summaryByCurrencyAndGroupBySubject = (yearMonth) -> {
            QueryVoucherItemOfSubLegerParam param = new QueryVoucherItemOfSubLegerParam();
            param.setCurrencyName(currencyName);
            param.setYearMonthNum(yearMonth);
            param.setLeftValue(dbSubject.getLeftValue());
            param.setRightValue(dbSubject.getRightValue());
            return voucherItemService.summaryVoucherItem(param, forEachVoucherItem);
        };

        YearMonth tmpYearMonth = startMonth;
        AccountBalance openingAccountBalance = null;
        while (tmpYearMonth.isBefore(endMonth) || tmpYearMonth.equals(endMonth)) {
            Map<Long, AccountBalance> accountBalanceBySubjectId =
                    accountBalanceService.summary(tmpYearMonth, subjects, summaryByCurrencyAndGroupBySubject);
            AccountBalance accountBalance = Optional.ofNullable(accountBalanceBySubjectId.get(subjectId))
                    .orElseGet(AccountBalance::newInstance);
            result.addAll(ReportConvert.INSTANCE.toSubLedgerVO(accountBalance, tmpYearMonth));

            if (tmpYearMonth.equals(startMonth)) {
                openingAccountBalance = accountBalance;
            }
            tmpYearMonth = tmpYearMonth.plusMonths(1);
        }

        BigDecimal debitOpeningAmount = openingAccountBalance.getDebitOpeningAmount();
        BigDecimal creditOpeningAmount = openingAccountBalance.getCreditOpeningAmount();
        AtomicReference<BigDecimal> openingBalance = new AtomicReference<>(debitOpeningAmount.subtract(creditOpeningAmount));

        SubLedgerVO openingSubLedger = new SubLedgerVO()
                .setVoucherDate(startMonth.atDay(1))
                .setSummary("期初余额")
                .setBalance(openingBalance.get());
        result.add(0, openingSubLedger);
        result.stream().filter(ledger -> ledger.getVoucherNumber()!=null)
            .forEach(ledger -> ledger.setBalance(
                openingBalance.updateAndGet(balance ->
                    ledger.getDebitAmount().subtract(ledger.getCreditAmount()).add(balance)
                )
            ));
        return result;
    }


}
