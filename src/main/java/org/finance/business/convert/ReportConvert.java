package org.finance.business.convert;

import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.VoucherItem;
import org.finance.business.web.vo.GeneralLedgerVO;
import org.finance.business.web.vo.SubLedgerVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

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
                .lendingDirection(closingAmountDirection > 0 ? GeneralLedgerVO.LendingDirection.DEBIT
                        : closingAmountDirection == 0 ? GeneralLedgerVO.LendingDirection.BALANCE
                        : GeneralLedgerVO.LendingDirection.CREDIT)
                .balance(closingAmountDirection > 0 ? debitClosingAmount
                        : closingAmountDirection == 0 ? BigDecimal.ZERO
                        : creditClosingAmount);
        List<GeneralLedgerVO> list = new ArrayList<>(3);
        if (visibleOpeningBalance) {
            BigDecimal debitOpeningAmount = accountBalance.getDebitOpeningAmount();
            BigDecimal creditOpeningAmount = accountBalance.getCreditOpeningAmount();
            list.add(builder.build().setSummary("期初余额")
                    .setBalance(debitOpeningAmount.subtract(creditOpeningAmount))
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
}
