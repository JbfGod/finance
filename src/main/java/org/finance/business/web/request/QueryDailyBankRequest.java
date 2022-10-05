package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author jiangbangfa
 */
@Data
public class QueryDailyBankRequest {

    private Long subjectId;
    @NotNull
    private LocalDate voucherDate;
    private String currencyName;

}
