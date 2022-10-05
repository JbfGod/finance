package org.finance.business.convert;

import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.InitialBalanceItem;
import org.finance.business.entity.VoucherItem;
import org.finance.business.web.vo.AccountBalanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        BigDecimal localDebitAmount = item.getLocalDebitAmount();
        BigDecimal localCreditAmount = item.getLocalCreditAmount();
        BigDecimal balance = debitAmount.subtract(creditAmount);
        BigDecimal localBalance = localDebitAmount.subtract(localCreditAmount);
        return AccountBalance.newInstance()
                .setSubjectId(item.getSubjectId())
                .setSubjectNumber(item.getSubjectNumber())
                .setCurrency(item.getCurrencyName())
                .setDebitCurrentAmount(localDebitAmount)
                .setCreditCurrentAmount(localCreditAmount)
                .setDebitAnnualAmount(localDebitAmount)
                .setCreditAnnualAmount(localCreditAmount)
                .calcClosingBalance(balance, localBalance);
    }

    default AccountBalance toAccountBalance(VoucherItem voucherItem) {
        BigDecimal debitAmount = voucherItem.getDebitAmount();
        BigDecimal creditAmount = voucherItem.getCreditAmount();
        BigDecimal localDebitAmount = voucherItem.getLocalDebitAmount();
        BigDecimal localCreditAmount = voucherItem.getLocalCreditAmount();
        return AccountBalance.newInstance()
                .setSubjectId(voucherItem.getSubjectId())
                .setSubjectNumber(voucherItem.getSubjectNumber())
                .setMaxVoucherNumber(voucherItem.getVoucherNumber())
                .setDebitAnnualAmount(localDebitAmount)
                .setCreditAnnualAmount(localCreditAmount)
                .setDebitCurrentAmount(localDebitAmount)
                .setCreditCurrentAmount(localCreditAmount)
                .calcClosingBalance(debitAmount.subtract(creditAmount), localDebitAmount.subtract(localCreditAmount));
    }

    default Map<String, AccountBalance> toMapByCurrency(List<AccountBalance> accountBalances) {
        return Optional.ofNullable(accountBalances).orElseGet(ArrayList::new)
                .stream().collect(Collectors.toMap(AccountBalance::getCurrency, v->v));
    }

    default Map<Long, List<AccountBalance>> toGroupBySubject(List<AccountBalance> accountBalances) {
        return Optional.ofNullable(accountBalances).orElseGet(ArrayList::new)
                .stream().collect(Collectors.groupingBy(AccountBalance::getSubjectId));
    }

    default Map<Long, AccountBalance> flatValuesByCurrency(Map<Long, List<AccountBalance>> balancesBySubject) {
        Map<Long, AccountBalance> balanceBySubject = new LinkedHashMap<>();
        balancesBySubject.forEach((subjectId, balances) -> {
            AccountBalance anyBalance = balances.get(0);
            Supplier<AccountBalance> newInstance = () -> AccountBalance.newInstance()
                    .setYear(anyBalance.getYear()).setYearMonthNum(anyBalance.getYearMonthNum())
                    .setSubjectId(subjectId).setSubjectNumber(anyBalance.getSubjectNumber());
            balanceBySubject.put(subjectId, this.sumAccountBalances(balances, newInstance));
        });
        return balanceBySubject;
    }

    default AccountBalance sumAccountBalances(List<AccountBalance> accountBalances, Supplier<AccountBalance> newInstance) {
        BigDecimal zero = new BigDecimal("0");
        BigDecimal debitCurrentBalance = zero, creditCurrentBalance = zero,
                openingBalance = zero, localOpeningBalance = zero,
                closingBalance = zero, localClosingBalance = zero,
                debitAnnualAmount = zero, creditAnnualAmount = zero;
        Integer maxVoucherNumber = 0;
        for (AccountBalance balance : accountBalances) {
            debitCurrentBalance = debitCurrentBalance.add(balance.getDebitCurrentAmount());
            creditCurrentBalance = creditCurrentBalance.add(balance.getCreditCurrentAmount());
            openingBalance = openingBalance.add(balance.getOpeningBalance());
            localOpeningBalance = localOpeningBalance.add(balance.getLocalOpeningBalance());
            closingBalance = closingBalance.add(balance.getClosingBalance());
            localClosingBalance = localClosingBalance.add(balance.getLocalClosingBalance());
            debitAnnualAmount = debitAnnualAmount.add(balance.getDebitAnnualAmount());
            creditAnnualAmount = creditAnnualAmount.add(balance.getCreditAnnualAmount());
            if (balance.getMaxVoucherNumber() != null && balance.getMaxVoucherNumber().compareTo(maxVoucherNumber) > 0) {
                maxVoucherNumber = balance.getMaxVoucherNumber();
            }
        }
        return newInstance.get()
                .setMaxVoucherNumber(maxVoucherNumber)
                .setDebitCurrentAmount(debitCurrentBalance)
                .setCreditCurrentAmount(creditCurrentBalance)
                .calcOpeningBalance(openingBalance, localOpeningBalance)
                .calcClosingBalance(closingBalance, localClosingBalance)
                .setDebitAnnualAmount(debitAnnualAmount)
                .setCreditAnnualAmount(creditAnnualAmount);
    }
}
