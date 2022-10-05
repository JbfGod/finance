package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class DailyCashVO {

    private Long subjectId;
    private String subjectNumber;
    private String subjectName;
    private String currency;
    private BigDecimal balanceOfYesterday;
    private BigDecimal localBalanceOfYesterday;
    private BigDecimal debitAmountOfToday;
    private BigDecimal localDebitAmountOfToday;
    private BigDecimal creditAmountOfToday;
    private BigDecimal localCreditAmountOfToday;
    private BigDecimal balanceOfToday;
    private BigDecimal localBalanceOfToday;
    private Integer debitTotal;
    private Integer creditTotal;


    public String getId() {
        return UUID.randomUUID().toString();
    }

    public String getKey() {
        return String.format("%s-%s", this.currency, this.subjectId);
    }

    public static DailyCashVO newInstance(String subjectName) {
        return new DailyCashVO()
                .setSubjectName(subjectName)
                .setBalanceOfYesterday(new BigDecimal("0"))
                .setLocalBalanceOfYesterday(new BigDecimal("0"))
                .setDebitAmountOfToday(new BigDecimal("0"))
                .setLocalDebitAmountOfToday(new BigDecimal("0"))
                .setCreditAmountOfToday(new BigDecimal("0"))
                .setLocalCreditAmountOfToday(new BigDecimal("0"))
                .setBalanceOfToday(new BigDecimal("0"))
                .setLocalBalanceOfToday(new BigDecimal("0"))
                .setDebitTotal(0)
                .setCreditTotal(0);

    }

    public DailyCashVO add(DailyCashVO dailyCash) {
        return this.setCurrency(dailyCash.getCurrency())
                .setBalanceOfYesterday(this.balanceOfYesterday.add(dailyCash.getBalanceOfYesterday()))
                .setLocalBalanceOfYesterday(this.localBalanceOfYesterday.add(dailyCash.getLocalBalanceOfYesterday()))
                .setDebitAmountOfToday(this.debitAmountOfToday.add(dailyCash.getDebitAmountOfToday()))
                .setLocalDebitAmountOfToday(this.localDebitAmountOfToday.add(dailyCash.getLocalDebitAmountOfToday()))
                .setCreditAmountOfToday(this.creditAmountOfToday.add(dailyCash.getCreditAmountOfToday()))
                .setLocalCreditAmountOfToday(this.localCreditAmountOfToday.add(dailyCash.getLocalCreditAmountOfToday()))
                .setBalanceOfToday(this.balanceOfToday.add(dailyCash.getBalanceOfToday()))
                .setLocalBalanceOfToday(this.localBalanceOfToday.add(dailyCash.getLocalBalanceOfToday()))
                .setDebitTotal(this.debitTotal + dailyCash.getDebitTotal())
                .setCreditTotal(this.creditTotal + dailyCash.getCreditTotal());
    }
}
