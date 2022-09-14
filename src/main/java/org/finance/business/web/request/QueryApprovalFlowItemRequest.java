package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.ApprovalFlow;

import javax.validation.constraints.NotNull;

/**
 * @author jiangbangfa
 */
@Data
public class QueryApprovalFlowItemRequest {

    @NotNull(message = "请选择业务模块！")
    private ApprovalFlow.BusinessModule businessModule;
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

}
