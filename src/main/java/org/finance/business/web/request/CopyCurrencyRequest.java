package org.finance.business.web.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
@Data
public class CopyCurrencyRequest {

    private final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");
    private Integer targetYearMonth;
    private Integer sourceYearMonth;

    private Boolean isOverride;

    public Integer getTargetYearMonth() {
        return targetYearMonth == null? Integer.valueOf(LocalDate.now().minusMonths(1).format(YEAR_MONTH_FMT))
                : targetYearMonth;
    }

    public Integer getSourceYearMonth() {
        return sourceYearMonth == null? Integer.valueOf(LocalDate.now().format(YEAR_MONTH_FMT))
                : sourceYearMonth;
    }

    public Boolean isOverride() {
        return isOverride != null && isOverride;
    }
}
