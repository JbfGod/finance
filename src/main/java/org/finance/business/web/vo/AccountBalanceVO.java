package org.finance.business.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 科目余额报表
 * @author jiangbangfa
 */
@Data
public class AccountBalanceVO {

    private Long subjectId;
    private String subjectNumber;
    private String subjectName;
    private BigDecimal debitOpeningAmount;
    private BigDecimal creditOpeningAmount;

    private BigDecimal debitCurrentAmount;
    private BigDecimal creditCurrentAmount;
    private BigDecimal debitAnnualAmount;
    private BigDecimal creditAnnualAmount;
    private BigDecimal debitClosingAmount;
    private BigDecimal creditClosingAmount;

}
