package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审批实例的审批项
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("approval_instance_item")
public class ApprovalInstanceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 审批流程实例
     */
    private Long instanceId;

    /**
     * 被审批的记录的ID
     */
    private Long moduleId;

    /**
     * 审批级别，从1开始
     */
    private Integer level;

    /**
     * 审批人
     */
    private String approver;

    /**
     * 审批用户
     */
    private Long approverId;

    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 是否是末级审批
     */
    private Boolean lasted;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modifyBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    public ApprovalInstanceItem() {
    }

    public ApprovalInstanceItem(ApprovalInstance approvalInstance, ApprovalFlowItem flowItem) {
        this.instanceId = approvalInstance.getId();
        this.level = flowItem.getLevel();
        this.moduleId = approvalInstance.getModuleId();
        this.passed = false;
    }
}
