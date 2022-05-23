package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateUserPasswordRequest {

    @NotNull(message = "请填写用户ID")
    private Long id;

    @NotBlank(message = "请填写密码！")
    @Size(min = 6, max = 20, message = "用户名只允许有6-20个字符！")
    private String password;

}
