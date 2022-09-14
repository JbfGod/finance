package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 审批通过
 * @author jiangbangfa
 */
@Data
public class ApprovedRequest {

    @NotNull(message = "审核流程项ID不能为空！")
    private Long approvalInstanceItemId;
    @NotNull(message = "审批的模块记录ID")
    private Long moduleRecordId;
    private String remark;

}
