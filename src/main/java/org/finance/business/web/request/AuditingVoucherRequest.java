package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jiangbangfa
 */
@Data
public class AuditingVoucherRequest {

    @NotNull(message = "请选择月份！")
    private Integer yearMonth;
    private Integer beginSerialNum;
    private Integer endSerialNum;

}
