package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 审批流相关的审核人员
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("approval_instance_approver")
public class ApprovalInstanceApprover implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    /**
     * 审批流实例ID
     */
    private Long instanceId;

    /**
     * 审批流的审批项ID
     */
    private Long itemId;

    /**
     * 被审批的记录的ID
     */
    private Long moduleId;

    /**
     * 审批人ID
     */
    private Long approverId;


}
