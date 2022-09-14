package org.finance.business.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class ApprovalFlowItemVO {

    private Long id;
    private Long approvalFlowId;
    private String department;
    private Integer level;
    private Boolean lasted;
    private List<Long> approverIds;

}
