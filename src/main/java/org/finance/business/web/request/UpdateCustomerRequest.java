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

    @NotNull(message = "请填写ID")
    private Long id;

    @NotBlank(message = "请填写客户名称！")
    @Size(min = 2, max = 20, message = "客户名称只允许有2-20个字符！")
    private String name;

    @NotNull(message = "请填写客户行业")
    private Long industryId;

    @NotNull(message = "请填写客户类型")
    private Customer.Type type;

    private Long businessUserId;

    private Boolean enabled;

    private LocalDateTime effectTime;

    private LocalDateTime expireTime;

    private String telephone;

    private String bankAccount;

    private String bankAccountName;

    @NotNull(message = "请选择是否使用外汇！")
    private Boolean useForeignExchange;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;

}
