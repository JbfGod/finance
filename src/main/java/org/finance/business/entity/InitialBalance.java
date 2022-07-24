package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.finance.business.entity.enums.AuditStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 初始余额
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("initial_balance")
public class InitialBalance implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 客户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long customerId;
    /**
     * 年份:yyyy
     */
    private Integer year;
    /**
     * 月份:yyyyMM
     */
    private Integer yearMonthNum;
    /**
     * 审核状态
     */
    private AuditStatus auditStatus;
    /**
     * 记账状态
     */
    private Boolean bookkeeping;
    /**
     * 记账人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long bookkeepingBy;

    /**
     * 记账人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String bookkeeperName;

    /**
     * 审核人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long auditBy;

    /**
     * 审核人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String auditorName;

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


}
