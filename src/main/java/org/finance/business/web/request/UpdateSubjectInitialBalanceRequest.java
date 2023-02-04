package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateSubjectInitialBalanceRequest {

    @NotNull(message = "ID不能为空")
    private Long id;
    private BigDecimal beginningBalance;
    private BigDecimal openingBalance;
    private BigDecimal debitAnnualAmount;
    private BigDecimal creditAnnualAmount;
}
