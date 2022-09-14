package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateApprovalFlowItemRequest {

    @NotNull(message = "ID不能为空！")
    private Long id;
    private String department;
    @NotNull(message = "至少配置一个审批人员")
    @Size(min = 1, message = "至少配置一个审批人员")
    private List<Long> approverIds;

}
