package org.finance.business.convert;

import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.BalanceSheetReport;
import org.finance.business.entity.CashFlowReport;
import org.finance.business.entity.ProfitReport;
import org.finance.business.entity.VoucherItem;
import org.finance.business.web.request.SaveBalanceSheetReportRequest;
import org.finance.business.web.request.SaveCashFlowReportRequest;
import org.finance.business.web.request.SaveProfitReportRequest;
import org.finance.business.web.vo.DailyBankVO;
import org.finance.business.web.vo.DailyCashVO;
import org.finance.business.web.vo.GeneralLedgerVO;
import org.finance.business.web.vo.ProfitOfMonthVO;
import org.finance.business.web.vo.SubLedgerVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface ReportConvert {

    ReportConvert INSTANCE = Mappers.getMapper(ReportConvert.class);

    default List<GeneralLedgerVO> toGeneralLedgerVO(AccountBalance accountBalance, String subjectName, boolean visibleOpeningBalance) {
        BigDecimal debitClosingAmount = accountBalance.getDebitClosingAmount();
        BigDecimal creditClosingAmount = accountBalance.getCreditClosingAmount();
        int closingAmountDirection = debitClosingAmount.compareTo(creditClosingAmount);
        GeneralLedgerVO.GeneralLedgerVOBuilder builder = GeneralLedgerVO.builder()
                .subjectId(accountBalance.getSubjectId())
                .subjectNumber(accountBalance.getSubjectNumber())
                .subjectName(subjectName)
                .month(accountBalance.getYearMonthNum() % 100)
                .lendingDirection(accountBalance.getClosingBalanceLendingDirection())
                .balance(closingAmountDirection > 0 ? debitClosingAmount
                        : closingAmountDirection == 0 ? BigDecimal.ZERO
                        : creditClosingAmount);
        List<GeneralLedgerVO> list = new ArrayList<>(3);
        if (visibleOpeningBalance) {
            list.add(builder.build().setSummary("期初余额").setBalance(accountBalance.getOpeningBalance())
            );
        }
        list.add(builder.build().setSummary("本期合计")
            .setMaxVoucherNumber(accountBalance.getMaxVoucherNumber())
            .setDebitAmount(accountBalance.getDebitCurrentAmount())
            .setCreditAmount(accountBalance.getCreditCurrentAmount())
        );
        list.add(builder.build().setSummary("本年累计")
            .setDebitAmount(accountBalance.getDebitAnnualAmount())
            .setCreditAmount(accountBalance.getCreditAnnualAmount())
        );
        return list;
    }

    default List<SubLedgerVO> toSubLedgerVO(AccountBalance accountBalance, YearMonth yearMonth) {
        List<SubLedgerVO> list = new ArrayList<>();
        list.add(new SubLedgerVO()
                .setVoucherDate(yearMonth.atEndOfMonth())
                .setSummary("本期合计")
                .setDebitAmount(accountBalance.getDebitCurrentAmount())
                .setCreditAmount(accountBalance.getCreditCurrentAmount())
        );
        list.add(new SubLedgerVO()
                .setVoucherDate(yearMonth.atEndOfMonth())
                .setSummary("本年累计")
                .setDebitAmount(accountBalance.getDebitCurrentAmount())
                .setCreditAmount(accountBalance.getCreditCurrentAmount())
        );
        return list;
    }

    default SubLedgerVO toSubLedgerVO(VoucherItem voucherItem) {
        BigDecimal debitAmount = voucherItem.getLocalDebitAmount();
        BigDecimal creditAmount = voucherItem.getLocalCreditAmount();
        BigDecimal balance = debitAmount.subtract(creditAmount);
        return new SubLedgerVO()
                .setVoucherDate(voucherItem.getVoucherDate())
                .setVoucherNumber(voucherItem.getVoucherNumber())
                .setSerialNumber(voucherItem.getSerialNumber())
                .setSummary(voucherItem.getSummary())
                .setDebitAmount(debitAmount)
                .setCreditAmount(creditAmount)
                .setBalance(balance);
    }

    default List<DailyCashVO> toDailyCashVOList(List<DailyCashVO> dailyCashesOfToday, Map<String, AccountBalance> balanceOfYesterdayByKey) {
        DailyCashVO subtotal = DailyCashVO.newInstance("小计");
        List<DailyCashVO> results = new ArrayList<>();
        for (DailyCashVO dailyCashOfToday : dailyCashesOfToday) {
            BigDecimal balance = dailyCashOfToday.getDebitAmountOfToday().subtract(dailyCashOfToday.getCreditAmountOfToday());
            BigDecimal localBalance = dailyCashOfToday.getLocalDebitAmountOfToday().subtract(dailyCashOfToday.getLocalCreditAmountOfToday());
            AccountBalance balanceOfYesterday = Optional.ofNullable(balanceOfYesterdayByKey.get(dailyCashOfToday.getKey())).orElseGet(AccountBalance::newInstance);
            dailyCashOfToday.setBalanceOfYesterday(balanceOfYesterday.getClosingBalance())
                    .setLocalBalanceOfYesterday(balanceOfYesterday.getLocalClosingBalance())
                    .setBalanceOfToday(balance.add(balanceOfYesterday.getClosingBalance()))
                    .setLocalBalanceOfToday(localBalance.add(balanceOfYesterday.getLocalClosingBalance()));
            results.add(dailyCashOfToday);
            // 累计
            subtotal.add(dailyCashOfToday);
        }
        results.add(subtotal);
        return results;
    }

    default DailyCashVO toSummaryTotalDailyCashVO(List<DailyCashVO> dailyCashes) {
        return dailyCashes.stream().filter(dailyCashVO -> "小计".equals(dailyCashVO.getSubjectName()))
                .reduce(DailyCashVO.newInstance("合计"), DailyCashVO::add)
                .setCurrency("本位币").setDebitAmountOfToday(null).setCreditAmountOfToday(null)
                .setBalanceOfYesterday(null).setBalanceOfToday(null);
    }

    default List<DailyBankVO> toDailyBankVOList(List<DailyBankVO> dailyCashesOfToday, Map<String, AccountBalance> balanceOfYesterdayByKey) {
        DailyBankVO subtotal = DailyBankVO.newInstance("小计");
        List<DailyBankVO> results = new ArrayList<>();
        for (DailyBankVO dailyCashOfToday : dailyCashesOfToday) {
            BigDecimal localBalance = dailyCashOfToday.getLocalDebitAmountOfToday().subtract(dailyCashOfToday.getLocalCreditAmountOfToday());
            AccountBalance balanceOfYesterday = Optional.ofNullable(balanceOfYesterdayByKey.get(dailyCashOfToday.getKey())).orElseGet(AccountBalance::newInstance);
            dailyCashOfToday.setLocalBalanceOfYesterday(balanceOfYesterday.getLocalClosingBalance())
                    .setLocalBalanceOfToday(localBalance.add(balanceOfYesterday.getLocalClosingBalance()));
            results.add(dailyCashOfToday);
            // 累计
            subtotal.add(dailyCashOfToday);
        }
        results.add(subtotal);
        return results;
    }

    default DailyBankVO toSummaryTotalDailyBankVO(List<DailyBankVO> dailyBanks) {
        return dailyBanks.stream().filter(dailyBankVO -> "小计".equals(dailyBankVO.getSubjectName()))
                .reduce(DailyBankVO.newInstance("合计"), DailyBankVO::add)
                .setCurrency("本位币");
    }

    default List<ProfitOfMonthVO> toProfitOfMonthVO(List<ProfitReport> profits, Map<Integer, ProfitReport.ProfitParam> profitByRowNum) {
        return profits.stream().map(profit -> {
            Integer rowNumber = profit.getRowNumber();
            ProfitOfMonthVO profitOfMonthVO = new ProfitOfMonthVO();
            profitOfMonthVO.setId(profit.getId())
                    .setName(profit.getName())
                    .setRowNumber(rowNumber)
                    .setFormula(profit.getFormula());
            if (rowNumber != null && rowNumber > 0) {
                ProfitReport.ProfitParam profitParam = profitByRowNum.get(rowNumber);
                profitOfMonthVO.setMonthlyAmount(profitParam.getMonthlyAmount())
                        .setAnnualAmount(profitParam.getAnnualAmount());
            }
            return profitOfMonthVO;
        }).collect(Collectors.toList());
    }

    default List<ProfitReport> toProfitReports(SaveProfitReportRequest request) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return request.getRows().stream().map(row -> new ProfitReport()
                .setName(row.getName())
                .setSerialNumber(atomicInteger.getAndIncrement())
                .setRowNumber(row.getRowNumber())
                .setYearMonthNum(request.getYearMonthNum())
                .setFormula(row.getFormula())
        ).collect(Collectors.toList());
    }

    default List<CashFlowReport> toCashFlowReports(SaveCashFlowReportRequest request) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return request.getRows().stream().map(row -> new CashFlowReport()
                .setName(row.getName())
                .setSerialNumber(atomicInteger.getAndIncrement())
                .setRowNumber(row.getRowNumber())
                .setYearMonthNum(request.getYearMonthNum())
                .setFormula(row.getFormula())
        ).collect(Collectors.toList());
    }

    default List<BalanceSheetReport> toBalanceSheetReports(SaveBalanceSheetReportRequest request) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return request.getRows().stream().map(row -> new BalanceSheetReport()
                .setSerialNumber(atomicInteger.getAndIncrement())
                .setAssetsName(row.getAssetsName())
                .setAssetsRowNumber(row.getAssetsRowNumber())
                .setAssetsFormula(row.getAssetsFormula())
                .setEquityName(row.getEquityName())
                .setEquityRowNumber(row.getEquityRowNumber())
                .setEquityFormula(row.getEquityFormula())
                .setYearMonthNum(request.getYearMonthNum())
        ).collect(Collectors.toList());
    }
}
