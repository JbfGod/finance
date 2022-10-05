package org.finance.business.mapper.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class DailyVoucherItemDTO {

    private Long subjectId;
    private String subjectNumber;
    private String subjectName;
    private String currency;
    private String summary;
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

}
