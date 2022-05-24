package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.User;

import javax.validation.constraints.NotNull;

/**
 * @author jiangbangfa
 */
@Data
public class QueryUserRequest extends AbstractPageRequest {

    private String name;
    private String customerNumber;
    private String account;
    @NotNull(message = "必须选择用户类型")
    private User.Role role;

}
