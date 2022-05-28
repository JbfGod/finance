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

/**
 * <p>
 * 货币汇率列表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 本币ID
     */
    public static final Currency LOCAL_CURRENCY = new Currency()
            .setId(0L).setNumber("0").setName("人民币").setRate(new BigDecimal("1"));

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
     * 货币编号
     */
    private String number;

    /**
     * 月份:yyyyMM
     */
    private Integer yearMonthNum;

    /**
     * 货币名称
     */
    private String name;

    /**
     * 汇率
     */
    private BigDecimal rate;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 审核状态
     */
    private String auditStatus;

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

    public enum Type {
        LOCAL,
        FOREIGN
    }
}
