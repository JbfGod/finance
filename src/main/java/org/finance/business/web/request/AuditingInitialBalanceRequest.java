package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class AuditingInitialBalanceRequest {

    @NotNull(message = "请选择月份！")
    private YearMonth yearMonthDate;

}
