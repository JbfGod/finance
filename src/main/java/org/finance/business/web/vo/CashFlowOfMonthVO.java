package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class CashFlowOfMonthVO {

    private Long id;
    private String name;
    private Integer rowNumber;
    private String formula;
    private BigDecimal annualAmount;
    private BigDecimal monthlyAmount;

}
