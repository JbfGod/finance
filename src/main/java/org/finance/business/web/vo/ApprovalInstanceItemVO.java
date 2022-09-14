package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class ApprovalInstanceItemVO {

    private Long id;
    private Integer level;
    private Long approverId;
    private String approver;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime approvalTime;
    private Boolean passed;
    private Boolean lasted;
    private String remark;
    private Boolean canApproved;

}
