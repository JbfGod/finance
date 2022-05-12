package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateCustomerRequest {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotBlank(message = "客户名称不能为空！")
    @Size(min = 2, max = 20, message = "客户名称只允许有2-20个字符！")
    private String name;

    @NotNull(message = "客户行业不能为空")
    private Long industryId;

    @NotNull(message = "客户类型不能为空")
    private Customer.Type type;

    private Boolean enabled;

    @NotNull(message = "请选择生效时间！")
    private LocalDateTime effectTime;

    @NotNull(message = "请选择过期时间！")
    private LocalDateTime expireTime;

    @NotNull(message = "请输入电话号码！")
    private String telephone;

    @NotNull(message = "请输入银行账号！")
    private String bankAccount;

    @NotNull(message = "请输入银行开户人名称！")
    private String bankAccountName;

    @NotNull(message = "请选择是否使用外汇！")
    private Boolean useForeignExchange;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;

}
