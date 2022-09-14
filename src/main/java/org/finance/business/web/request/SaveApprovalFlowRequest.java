package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.ApprovalFlow;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class SaveApprovalFlowRequest {

    @NotNull(message = "客户ID不能为空！")
    private Long customerId;
    @NotNull(message = "请选择业务模块！")
    private ApprovalFlow.BusinessModule businessModule;
    @NotNull(message = "至少配置一项审批流程！")
    @Size(min = 1, max = 6, message = "至少配置一项审批流程，至多配置6项审批流程！")
    private List<AddApprovalFlowRequest.FlowItem> flowItems;

    @Data
    public static class FlowItem {

        private String department;
        @NotNull(message = "至少配置一个审批人员")
        @Size(min = 1, message = "至少配置一个审批人员")
        private List<Long> approverIds;

    }

}
