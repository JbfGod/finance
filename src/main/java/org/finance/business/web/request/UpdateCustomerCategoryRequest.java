package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateCustomerCategoryRequest {

    @NotNull(message = "请填写类别ID")
    private Long id;

    @NotBlank(message = "请填写类别名称！")
    @Size(min = 2, max = 20, message = "类别名称只允许有2-20个字符！")
    private String name;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;

}
