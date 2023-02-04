package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.infrastructure.constants.LendingDirection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
     * 科目编号
     */
    private String number;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目类别
     */
    private Category category;

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

    private String path;
    /**
     * 是否有叶子节点
     */
    private Boolean hasLeaf;

    /**
     * 节点深度
     */
    private Integer level;

    /**
     * 根级别ID
     */
    private String rootNumber;

    private Long rootId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 年初余额
     */
    private BigDecimal beginningBalance;
    /**
     * 期初余额
     */
    private BigDecimal openingBalance;
    /**
     * 借方年累计金额
     */
    private BigDecimal debitAnnualAmount;
    /**
     * 贷方年累计金额
     */
    private BigDecimal creditAnnualAmount;
    /**
     * 现金流量科目
     */
    private Boolean beCashFlow;

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
    private List<Subject> children;

    /**
     * 科目类别
     */
    public enum Category {
        /**
         * 资产
         */
        ASSETS,
        /**
         * 负债
         */
        FU_ZAI,
        /**
         * 权益
         */
        EQUITY,
        /**
         * 成本
         */
        COST,
        /**
         * 损益
         */
        PROFIT,
        /**
         * 共同
         */
        COMMON,
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

    public Subject() {}

    public Subject(String number, String name,
                   Category category, LendingDirection lendingDirection) {
        this.number = number;
        this.name = name;
        this.category = category;
        this.lendingDirection = lendingDirection;
    }

    public Subject(String number, String name, Category category,
                   LendingDirection lendingDirection, Boolean beCashFlow) {
        this.number = number;
        this.name = name;
        this.category = category;
        this.lendingDirection = lendingDirection;
        this.beCashFlow = beCashFlow;
    }
}
