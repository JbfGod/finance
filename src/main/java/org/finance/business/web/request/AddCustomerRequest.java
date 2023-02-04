package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Customer;
import org.finance.business.entity.enums.AccountingSystem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class AddCustomerRequest {

    @NotBlank(message = "请填写客户编号！")
    @Size(min = 5, max = 25, message = "客户编号只允许有5-25个字符！")
    @Pattern(regexp = "[\\da-zA-Z]{5,25}", message = "客户编号只允许包含数字和字母")
    private String number;

    @NotBlank(message = "请填写客户名称！")
    @Size(min = 2, max = 20, message = "客户名称只允许有2-20个字符！")
    private String name;

    @NotNull(message = "请填写客户类别")
    private Long categoryId;

    @NotNull(message = "请填写客户类型")
    private Customer.Type type;
    @NotNull(message = "请选择会计制度")
    private AccountingSystem accountingSystem;

    private Long businessUserId;

    private Boolean enabled;

    private LocalDateTime effectTime;

    private LocalDateTime expireTime;

    private String contactName;

    private String telephone;

    private String bankAccount;

    private String bankAccountName;

    @NotNull(message = "请选择是否使用外汇！")
    private Boolean useForeignExchange;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;
    @NotNull(message = "启用日期不能为空")
    private Integer enablePeriod;

    public Integer getCurrentPeriod() {
        return this.enablePeriod;
    }
}
