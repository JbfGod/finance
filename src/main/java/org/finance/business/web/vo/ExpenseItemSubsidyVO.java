package org.finance.business.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class ExpenseItemSubsidyVO {

    private Long billId;
    private Long itemId;
    private Long subjectId;
    private String name;
    private Integer days;
    private BigDecimal amountForDay;
    private BigDecimal amount;

}
