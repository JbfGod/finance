package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 科目表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Data
@Accessors(chain = true)
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属客户
     */
    private Long customerId;

    /**
     * 所属行业
     */
    private Long industryId;

    /**
     * 科目编号
     */
    private String number;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目类型
     */
    private Type type;

    /**
     * 辅助结算
     */
    private AssistSettlement assistSettlement;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 父级编号
     */
    private String parentNumber;

    /**
     * 是否有叶子节点
     */
    private Boolean hasLeaf;

    /**
     * 节点深度
     */
    private Integer level;

    /**
     * 节点左值
     */
    private Integer leftValue;

    /**
     * 节点右值
     */
    private Integer rightValue;

    /**
     * 根级别ID
     */
    private Long rootId;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    @TableLogic
    private Boolean deleted;

    public enum Type {
        /**
         * 科目
         */
        SUBJECT,
        /**
         * 花费
         */
        COST,
        /**
         * 科目+花费
         */
        SUBJECT_AND_COST,
        ;
    }

    public enum AssistSettlement {
        /**
         * 无
         */
        NOTHING,
        /**
         * 供应商
         */
        SUPPLIER,
        /**
         * 客户
         */
        CUSTOMER,
        /**
         * 员工
         */
        EMPLOYEE,
        /**
         * 银行
         */
        BANK,
        ;
    }

}
