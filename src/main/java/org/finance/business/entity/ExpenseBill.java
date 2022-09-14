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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用报销单
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("expense_bill")
public class ExpenseBill implements Serializable {

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

    @TableField(fill = FieldFill.INSERT)
    private String customerNumber;

    /**
     * 报销单号
     */
    private String number;

    /**
     * 报销人
     */
    private String expensePerson;

    /**
     * 合计补助金额
     */
    private BigDecimal totalSubsidyAmount;

    /**
     * 总票据张数
     */
    private Integer totalNumOfBill;

    /**
     * 总票据金额
     */
    private BigDecimal totalBillAmount;

    /**
     * 总实际金额
     */
    private BigDecimal totalActualAmount;

    /**
     * 总小计费用金额
     */
    private BigDecimal totalSubtotalAmount;

    /**
     * 报销日期
     */
    private LocalDateTime expenseTime;

    /**
     * 职位
     */
    private String position;

    /**
     * 报销事由
     */
    private String reason;

    /**
     * 审核状态
     */
    private AuditStatus auditStatus;

    /**
     * 流程审批ID，0表示还未开启审批流程
     */
    private Long approvalFlowInstanceId;

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
    private List<ExpenseItem> items;

}