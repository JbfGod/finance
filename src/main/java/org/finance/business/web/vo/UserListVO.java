package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.User;

/**
 * @author jiangbangfa
 */
@Data
public class UserListVO {

    private Long id;
    private User.Role role;
    private Long customerId;
    private String name;
    private String customerNumber;
    private String customerName;
    private String account;

}
