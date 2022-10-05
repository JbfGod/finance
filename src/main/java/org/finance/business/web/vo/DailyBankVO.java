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
public class DailyBankVO {

    private Long subjectId;
    private String subjectNumber;
    private String subjectName;
    private String currency;
    private String summary;
    private BigDecimal localBalanceOfYesterday;
    private BigDecimal localDebitAmountOfToday;
    private BigDecimal localCreditAmountOfToday;
    private BigDecimal localBalanceOfToday;
    private Integer debitTotal;
    private Integer creditTotal;


    public String getId() {
        return UUID.randomUUID().toString();
    }

    public String getKey() {
        return String.format("%s-%s", this.currency, this.subjectId);
    }

    public static DailyBankVO newInstance(String subjectName) {
        return new DailyBankVO()
                .setSubjectName(subjectName)
                .setLocalBalanceOfYesterday(new BigDecimal("0"))
                .setLocalDebitAmountOfToday(new BigDecimal("0"))
                .setLocalCreditAmountOfToday(new BigDecimal("0"))
                .setLocalBalanceOfToday(new BigDecimal("0"))
                .setDebitTotal(0)
                .setCreditTotal(0);

    }

    public DailyBankVO add(DailyBankVO dailyCash) {
        return this.setCurrency(dailyCash.getCurrency())
                .setLocalBalanceOfYesterday(this.localBalanceOfYesterday.add(dailyCash.getLocalBalanceOfYesterday()))
                .setLocalDebitAmountOfToday(this.localDebitAmountOfToday.add(dailyCash.getLocalDebitAmountOfToday()))
                .setLocalCreditAmountOfToday(this.localCreditAmountOfToday.add(dailyCash.getLocalCreditAmountOfToday()))
                .setLocalBalanceOfToday(this.localBalanceOfToday.add(dailyCash.getLocalBalanceOfToday()))
                .setDebitTotal(this.debitTotal + dailyCash.getDebitTotal())
                .setCreditTotal(this.creditTotal + dailyCash.getCreditTotal());
    }
}
