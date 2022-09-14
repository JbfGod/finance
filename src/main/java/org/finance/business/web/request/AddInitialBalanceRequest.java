package org.finance.business.web.request;

import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class AddInitialBalanceRequest {

    @NotNull(message = "请选择月份！")
    private YearMonth yearMonthDate;
    @NotNull(message = "币别名称不能为空！")
    private String currencyName;
    @NotNull(message = "请选择科目！")
    private Long subjectId;
    @NotBlank(message = "请选择科目！")
    private String subjectNumber;
    @NotNull(message = "请选择借贷方向！")
    private LendingDirection lendingDirection;
    @NotNull(message = "请输入原币金额！")
    private BigDecimal amount;

    public Integer getYear() {
        return yearMonthDate.getYear();
    }

    public Integer getYearMonthNum() {
        return yearMonthDate.getYear() * 100 + yearMonthDate.getMonthValue();
    }

}
