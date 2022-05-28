package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 凭证
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class Voucher implements Serializable {

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
     * 凭证日期
     */
    private LocalDateTime voucherTime;

    /**
     * 原币ID
     */
    private Long currencyId;

    /**
     * 原币汇率
     */
    private BigDecimal rate;

    /**
     * 原币名称
     */
    private String currencyName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 凭证序号,每月凭证从1开始
     */
    private Integer serialNumber;

    /**
     * 附件张数
     */
    private Integer attachmentNum;

    /**
     * 原币合计金额
     */
    private BigDecimal totalCurrencyAmount;

    /**
     * 本币合计金额
     */
    private BigDecimal totalLocalCurrencyAmount;

    /**
     * 审核状态
     */
    private String auditStatus;

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
    private List<VoucherItem> items;
}