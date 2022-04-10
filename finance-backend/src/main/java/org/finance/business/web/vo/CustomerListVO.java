package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.Customer;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class CustomerListVO {

    private Long id;

    private String account;

    private String name;

    private String industry;

    private String category;

    private Customer.Type type;

    private Boolean enabled;

    private LocalDateTime effectTime;

    private LocalDateTime expireTime;

    private String telephone;

    private String bankAccount;

    private String bankAccountName;

    private Boolean useForeignExchange;

    private String remark;

}
