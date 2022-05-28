package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.infrastructure.constants.LendingDirection;

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

    @TableField(fill = FieldFill.INSERT)
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
     * 借贷方向
     */
    private LendingDirection lendingDirection;

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
    private String rootNumber;

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

    /**
     * 科目类型
     */
    public enum Type {
        /**
         * 科目
         */
        SUBJECT,
        /**
         * 费用
         */
        COST,
        /**
         * 科目+费用
         */
        SUBJECT_AND_COST,
        ;
    }

    /**
     * 辅助结算
     */
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
