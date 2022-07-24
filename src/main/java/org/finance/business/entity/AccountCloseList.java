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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 关账列表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_close_list")
public class AccountCloseList implements Serializable {

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
     * 关账开始日期
     */
    private LocalDate beginDate;

    /**
     * 关账结束日期
     */
    private LocalDate endDate;

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
