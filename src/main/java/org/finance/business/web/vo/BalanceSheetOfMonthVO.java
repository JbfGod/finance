package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class BalanceSheetOfMonthVO {

    private Long id;
    private BigDecimal assetsOpeningAmount;
    private BigDecimal assetsClosingAmount;
    private BigDecimal equityOpeningAmount;
    private BigDecimal equityClosingAmount;
    private String assetsName;
    private Integer assetsRowNumber;
    private String assetsFormula;
    private String equityName;
    private Integer equityRowNumber;
    private String equityFormula;

}
