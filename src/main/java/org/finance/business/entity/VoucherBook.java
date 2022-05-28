package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 凭证账簿
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("voucher_book")
public class VoucherBook implements Serializable {

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
     * 客户编号
     */
    @TableField(fill = FieldFill.INSERT)
    private String customerNumber;

    /**
     * 年份:yyyy
     */
    private Integer year;

    /**
     * 月份:yyyyMM
     */
    private Integer yearMonthNum;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目编号
     */
    private String subjectNumber;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 凭证日期
     */
    private LocalDateTime voucherTime;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 借方金额
     */
    private BigDecimal debitsAmount;

    /**
     * 贷方金额
     */
    private BigDecimal creditsAmount;

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
