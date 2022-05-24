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

    private String customerNumber;

    private String account;

    private User.Role role;

    private Long createBy;

    private LocalDateTime createTime;

    private Long modifyBy;

    private LocalDateTime modifyTime;
}
