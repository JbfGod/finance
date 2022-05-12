package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.User;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class UserSelfVO {

    private Long id;

    private Long customerId;

    private String name;

    private String customerAccount;

    private String account;

    private User.Role role;

    private String createBy;

    private LocalDateTime createTime;

    private String modifyBy;

    private LocalDateTime modifyTime;
}
