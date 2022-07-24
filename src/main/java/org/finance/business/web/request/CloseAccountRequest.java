package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class CloseAccountRequest {


    @NotNull(message = "请选择关账的月份！")
    private YearMonth yearMonthDate;

    public Integer getYear() {
        return yearMonthDate.getYear();
    }

    public Integer getYearMonthNum() {
        return yearMonthDate.getYear() * 100 + yearMonthDate.getMonthValue();
    }

    public LocalDate getBeginDate() {
        return yearMonthDate.atDay(1);
    }

    public LocalDate getEndDate() {
        return yearMonthDate.atEndOfMonth();
    }

}
