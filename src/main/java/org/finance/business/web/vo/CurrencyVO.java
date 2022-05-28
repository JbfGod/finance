package org.finance.business.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class CurrencyVO {

    private Long id;
    private Long customerId;
    private String number;
    private String yearMonthNum;
    private String name;
    private BigDecimal rate;
    private String remark;
    private String auditStatus;
    private String creatorName;

}
