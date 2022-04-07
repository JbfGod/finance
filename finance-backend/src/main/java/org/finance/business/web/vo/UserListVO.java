package org.finance.business.web.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class UserListVO {

    private Long id;

    private String name;

    private String customerAccount;

    private String account;

    private String createBy;

    private LocalDateTime createTime;

    private String modifyBy;

    private LocalDateTime modifyTime;

}
