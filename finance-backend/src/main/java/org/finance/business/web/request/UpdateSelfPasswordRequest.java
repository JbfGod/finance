package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateSelfPasswordRequest {

    @NotBlank(message = "密码不能为空！")
    @Size(min = 6, max = 20, message = "用户名只允许有6-20个字符！")
    private String oldPassword;

    @NotBlank(message = "密码不能为空！")
    @Size(min = 6, max = 20, message = "用户名只允许有6-20个字符！")
    private String newPassword;

}
