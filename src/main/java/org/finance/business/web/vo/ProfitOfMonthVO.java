package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class ProfitOfMonthVO {

    private Long id;
    private String name;
    private Integer rowNum;
    private String formula;
    private BigDecimal annualAmount;
    private BigDecimal monthlyAmount;

}
