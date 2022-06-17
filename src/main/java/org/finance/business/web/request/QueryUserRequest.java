package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.User;

/**
 * @author jiangbangfa
 */
@Data
public class QueryUserRequest extends AbstractPageRequest {

    private String name;
    private String customerNumber;
    private String account;
    private User.Role role;

}
