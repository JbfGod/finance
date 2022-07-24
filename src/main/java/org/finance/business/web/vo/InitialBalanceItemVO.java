package org.finance.business.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class InitialBalanceItemVO {

    private Long id;
    private Long initialBalanceId;
    private Integer year;
    private Integer yearMonthNum;
    private Long subjectId;
    private String subjectNumber;
    private String subjectName;
    private String lendingDirection;
    private String currencyName;
    private BigDecimal amount;

}
