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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用报销条目
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("expense_item")
public class ExpenseItem implements Serializable {

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
     * 对应的报销单ID
     */
    private Long billId;

    /**
     * 序号
     */
    private Integer serialNumber;

    /**
     * 费用名称对应的科目
     */
    private Long subjectId;

    private String subjectNumber;

    /**
     * 开始日期
     */
    private LocalDateTime beginTime;

    /**
     * 结束日期
     */
    private LocalDateTime endTime;

    /**
     * 出差起讫地点
     */
    private String travelPlace;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 票据张数
     */
    private Integer numOfBill;

    /**
     * 票据金额
     */
    private BigDecimal billAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 补助费用金额
     */
    private BigDecimal subsidyAmount;

    /**
     * 小计费用金额
     */
    private BigDecimal subtotalAmount;

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

    @TableField(exist = false)
    private List<ExpenseItemSubsidy> subsidies;

    @TableField(exist = false)
    private List<ExpenseItemAttachment> attachments;
}
