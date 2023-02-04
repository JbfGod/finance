package org.finance.business.convert;

import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.Report;
import org.finance.business.entity.VoucherItem;
import org.finance.business.entity.templates.accountting.system.AssetsLiability;
import org.finance.business.entity.templates.accountting.system.CashFlow;
import org.finance.business.entity.templates.accountting.system.Profit;
import org.finance.business.web.vo.DailyBankVO;
import org.finance.business.web.vo.DailyCashVO;
import org.finance.business.web.vo.GeneralLedgerVO;
import org.finance.business.web.vo.SubLedgerVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface ReportConvert {

    ReportConvert INSTANCE = Mappers.getMapper(ReportConvert.class);

    Report clone(Report report);

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

    default List<DailyCashVO> toDailyCashVOList(List<DailyCashVO> dailyCashesOfToday, Map<Long, AccountBalance> balanceOfYesterdayByKey) {
        DailyCashVO subtotal = DailyCashVO.newInstance("小计");
        List<DailyCashVO> results = new ArrayList<>();
        for (DailyCashVO dailyCashOfToday : dailyCashesOfToday) {
            BigDecimal balance = dailyCashOfToday.getDebitAmountOfToday().subtract(dailyCashOfToday.getCreditAmountOfToday());
            BigDecimal localBalance = dailyCashOfToday.getLocalDebitAmountOfToday().subtract(dailyCashOfToday.getLocalCreditAmountOfToday());
            AccountBalance balanceOfYesterday = Optional.ofNullable(balanceOfYesterdayByKey.get(dailyCashOfToday.getSubjectId())).orElseGet(AccountBalance::newInstance);
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

    default List<DailyBankVO> toDailyBankVOList(List<DailyBankVO> dailyCashesOfToday, Map<Long, AccountBalance> balanceOfYesterdayByKey) {
        DailyBankVO subtotal = DailyBankVO.newInstance("小计");
        List<DailyBankVO> results = new ArrayList<>();
        for (DailyBankVO dailyCashOfToday : dailyCashesOfToday) {
            BigDecimal localBalance = dailyCashOfToday.getLocalDebitAmountOfToday().subtract(dailyCashOfToday.getLocalCreditAmountOfToday());
            AccountBalance balanceOfYesterday = Optional.ofNullable(balanceOfYesterdayByKey.get(dailyCashOfToday.getSubjectId())).orElseGet(AccountBalance::newInstance);
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

    default List<AssetsLiability> toAssetsLiabilities(List<Report> reports) {
        if (reports == null || reports.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, Report> reportById = reports.stream().collect(Collectors.toMap(Report::getId, v -> v));
        List<AssetsLiability> list = new ArrayList<>();
        for (int row1 = 1, row2 = 51; row1 <= 50 && row2 <= 100; row1++, row2++) {
            Report report1 = reportById.get(row1);
            Report report2 = reportById.get(row2);
            if (report1 == null && report2 == null) {
                continue;
            }
            list.add(new AssetsLiability(report1, report2));
        }
        return list;
    }

    default List<Profit> toProfit(List<Report> reports) {
        if (reports == null || reports.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, Report> reportById = reports.stream().collect(Collectors.toMap(Report::getId, v -> v));
        List<Profit> list = new ArrayList<>();
        int minId = Report.Category.PROFIT.getMinId();
        int maxId = Report.Category.PROFIT.getMaxId();
        for (; minId <= maxId; minId++) {
            Report report = reportById.get(minId);
            if (report == null) {
                continue;
            }
            list.add(new Profit(report));
        }
        return list;
    }

    default List<CashFlow> toCashFlow(List<Report> reports) {
        if (reports == null || reports.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, Report> reportById = reports.stream().collect(Collectors.toMap(Report::getId, v -> v));
        List<CashFlow> list = new ArrayList<>();
        int minId = Report.Category.CASH_FLOW.getMinId();
        int maxId = Report.Category.CASH_FLOW.getMaxId();
        for (; minId <= maxId; minId++) {
            Report report = reportById.get(minId);
            if (report == null) {
                continue;
            }
            list.add(new CashFlow(report));
        }
        return list;
    }
}
