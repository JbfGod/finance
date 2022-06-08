package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class AddUserRequest {

    private String customerNumber;

    @NotBlank(message = "请填写用户姓名！")
    @Size(min = 2, max = 20, message = "用户姓名只允许有2-20个字符！")
    private String name;

    @NotBlank(message = "请填写登录账号！")
    @Size(min = 5, max = 25, message = "登录账号只允许有5-25个字符！")
    @Pattern(regexp = "[\\da-zA-Z]{5,25}", message = "登录账号只允许包含数字和字母")
    private String account;

    @NotBlank(message = "请填写密码！")
    @Size(min = 6, max = 20, message = "密码只允许有6-20个字符！")
    private String password;

    @NotNull(message = "请填写用户类型！")
    private User.Role role;
}
