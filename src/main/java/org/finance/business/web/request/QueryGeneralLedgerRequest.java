package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class QueryGeneralLedgerRequest {

    @NotNull
    private YearMonth startMonth;
    @NotNull
    private YearMonth endMonth;
    @NotNull
    private String currencyName;

}
