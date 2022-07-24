package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class CancelClosedAccountRequest {

    @NotNull(message = "请选择取消关账的月份！")
    private YearMonth yearMonthDate;

    public Integer getYearMonthNum() {
        return yearMonthDate.getYear() * 100 + yearMonthDate.getMonthValue();
    }
}
