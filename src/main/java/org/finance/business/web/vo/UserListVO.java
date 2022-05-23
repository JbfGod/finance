package org.finance.business.web.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class UserListVO {

    private Long id;

    private Long customerId;

    private String name;

    private String customerAccount;

    private String account;

    private Long createBy;

    private LocalDateTime createTime;

    private Long modifyBy;

    private LocalDateTime modifyTime;

}
