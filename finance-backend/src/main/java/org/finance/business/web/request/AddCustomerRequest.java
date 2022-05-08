package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Customer;

import javax.validation.Valid;
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

    @NotBlank(message = "客户编号不能为空！")
    @Size(min = 5, max = 25, message = "客户编号只允许有5-25个字符！")
    @Pattern(regexp = "[0-9a-zA-Z]{5,25}", message = "客户编号只允许包含数字和字母")
    private String account;

    @NotBlank(message = "客户名称不能为空！")
    @Size(min = 2, max = 20, message = "客户名称只允许有2-20个字符！")
    private String name;

    @NotNull(message = "客户行业不能为空")
    private Long industryId;

    @NotNull(message = "客户类别不能为空")
    private Long categoryId;

    @NotNull(message = "客户类型不能为空")
    private Customer.Type type;

    private Boolean enabled;

    @NotNull(message = "请选择生效时间！")
    private LocalDateTime effectTime;

    @NotNull(message = "请选择过期时间！")
    private LocalDateTime expireTime;

    @NotNull(message = "联系人不能为空！")
    private String contactName;

    @NotNull(message = "联系电话不能为空！")
    private String telephone;

    @NotNull(message = "银行账号不能为空！")
    private String bankAccount;

    @NotNull(message = "开户人不能为空！")
    private String bankAccountName;

    @NotNull(message = "请选择是否使用外汇！")
    private Boolean useForeignExchange;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;

    @Valid
    private AddUserRequest user;
}
