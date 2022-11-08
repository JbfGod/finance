package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class QueryBalanceSheetReportRequest {

    @NotNull
    private YearMonth yearMonth;

}
