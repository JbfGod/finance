package org.finance.business.convert;

import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.InitialBalanceItem;
import org.finance.business.entity.VoucherItem;
import org.finance.business.web.vo.AccountBalanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Mapper
public interface AccountBalanceConvert {

    AccountBalanceConvert INSTANCE = Mappers.getMapper( AccountBalanceConvert.class );

    AccountBalanceVO toAccountBalance(AccountBalance accountBalance);

    default AccountBalance toAccountBalance(InitialBalanceItem item) {
        BigDecimal debitAmount = item.getDebitAmount();
        BigDecimal creditAmount = item.getCreditAmount();
        return AccountBalance.newInstance()
                .setSubjectId(item.getSubjectId())
                .setDebitCurrentAmount(debitAmount)
                .setCreditCurrentAmount(creditAmount)
                .setDebitAnnualAmount(debitAmount)
                .setCreditAnnualAmount(creditAmount)
                .calcClosingBalance(debitAmount, creditAmount);
    }

    default AccountBalance toAccountBalance(VoucherItem voucherItem) {
        BigDecimal currentDebitAmount = voucherItem.getLocalDebitAmount();
        BigDecimal currentCreditAmount = voucherItem.getLocalCreditAmount();
        return AccountBalance.newInstance()
                .setSubjectId(voucherItem.getSubjectId())
                .setDebitAnnualAmount(currentDebitAmount)
                .setCreditAnnualAmount(currentCreditAmount)
                .setDebitCurrentAmount(currentDebitAmount)
                .setCreditCurrentAmount(currentCreditAmount)
                .calcClosingBalance(currentDebitAmount, currentCreditAmount);
    }

}
