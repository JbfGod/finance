package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateUserRequest {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "用户姓名不能为空！")
    @Size(min = 2, max = 20, message = "用户姓名只允许有2-20个字符！")
    private String name;

}
