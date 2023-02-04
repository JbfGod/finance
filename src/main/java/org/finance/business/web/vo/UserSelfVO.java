package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.business.entity.User;
import org.finance.infrastructure.common.UserRedisContextState;

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

    private Customer customer;
    private Customer proxyCustomer;
    private UserRedisContextState state;

    @Data
    @Accessors(chain = true)
    public static class Customer {
        private Long id;
        private String name;
        private String number;
        private Integer enablePeriod;
        private Integer currentPeriod;
    }

}
