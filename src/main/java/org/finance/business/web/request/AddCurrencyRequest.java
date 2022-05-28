package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class AddCurrencyRequest {

    @NotBlank(message = "请输入货币编号！")
    private String number;
    @NotNull(message = "请选择月份")
    private Integer yearMonthNum;
    @NotBlank(message = "请输入货币名称！")
    private String name;
    @NotNull(message = "请输入货币汇率")
    private BigDecimal rate;
    private String remark;

}
