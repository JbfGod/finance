package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.finance.business.entity.Customer;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class CustomerListVO {

    private Long id;

    private String number;

    private String name;

    private Long businessUserId;

    private String contactName;

    private Long industryId;

    private String category;

    private Customer.Type type;

    private Boolean enabled;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime effectTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expireTime;

    private String telephone;

    private String bankAccount;

    private String bankAccountName;

    private Boolean useForeignExchange;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private Integer enablePeriod;

    public String getEnablePeriod() {
        return String.format("%s-%s", enablePeriod / 100, enablePeriod % 100);
    }

    public Long getBusinessUserId() {
        return this.businessUserId == 0 ? null : this.businessUserId;
    }
}
