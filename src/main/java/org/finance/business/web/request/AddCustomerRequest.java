package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Customer;

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

    @NotNull(message = "请填写客户行业")
    private Long industryId;

    @NotNull(message = "请填写客户类别")
    private Long categoryId;

    @NotNull(message = "请填写客户类型")
    private Customer.Type type;

    private Boolean enabled;

    @NotNull(message = "请选择生效时间！")
    private LocalDateTime effectTime;

    @NotNull(message = "请选择过期时间！")
    private LocalDateTime expireTime;

    @NotNull(message = "请填写联系人！")
    private String contactName;

    @NotNull(message = "请填写联系电话！")
    private String telephone;

    @NotNull(message = "请填写银行账号！")
    private String bankAccount;

    @NotNull(message = "请填写开户人！")
    private String bankAccountName;

    @NotNull(message = "请选择是否使用外汇！")
    private Boolean useForeignExchange;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;

}
