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
import java.util.List;

/**
 * <p>
 * 审批实例
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("approval_instance")
public class ApprovalInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    /**
     * 所属业务模块
     */
    private ApprovalFlow.BusinessModule businessModule;

    /**
     * 被审批的记录的ID
     */
    private Long moduleId;

    /**
     * 审批流程ID
     */
    private Long approvalFlowId;

    /**
     * 当前所处审批项ID
     */
    private Long currentItemId;

    /**
     * 当前所处审批级别
     */
    private Integer currentLevel;

    /**
     * 审批实例是否结束
     */
    private Boolean ended;

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

    @TableField(exist = false)
    private List<ApprovalInstanceItem> items;

    public ApprovalInstance() {
    }

    public ApprovalInstance(ApprovalFlow approvalFlow) {
        this.customerId = approvalFlow.getCustomerId();
        this.approvalFlowId = approvalFlow.getId();
        this.businessModule = approvalFlow.getBusinessModule();
        this.currentLevel = 1;
        this.ended = false;
    }
}
