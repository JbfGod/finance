package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class AddUserRequest {

    @NotBlank(message = "用户姓名不能为空！")
    @Size(min = 2, max = 20, message = "用户姓名只允许有2-20个字符！")
    private String name;

    @NotBlank(message = "登录账号不能为空！")
    @Size(min = 5, max = 25, message = "登录账号只允许有5-25个字符！")
    @Pattern(regexp = "[0-9a-zA-Z]{5,25}", message = "登录账号只允许包含数字和字母")
    private String account;

    @NotBlank(message = "密码不能为空！")
    @Size(min = 6, max = 20, message = "密码只允许有6-20个字符！")
    private String password;
}
