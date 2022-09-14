package org.finance.business.web.request;

import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateInitialBalanceRequest {

    private final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    @NotNull(message = "ID不能为空！")
    private Long id;
    @NotNull(message = "请选择币别！")
    private Long currencyId;
    @NotNull(message = "币别名称不能为空！")
    private String currencyName;
    @NotNull(message = "汇率不能为空！")
    private BigDecimal rate;
    @NotNull(message = "请选择科目！")
    private Long subjectId;
    @NotBlank(message = "请选择科目！")
    private String subjectNumber;
    @NotNull(message = "请选择借贷方向！")
    private LendingDirection lendingDirection;
    @NotNull(message = "请输入原币金额！")
    private BigDecimal amount;
}
