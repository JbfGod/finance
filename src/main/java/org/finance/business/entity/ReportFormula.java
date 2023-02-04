package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.finance.business.entity.enums.Symbol;

import java.io.Serializable;

/**
 * <p>
 * 报表条目计算公式(科目)
 * </p>
 *
 * @author jiangbangfa
 * @since 2023-01-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("report_formula")
public class ReportFormula extends AbstractFormula implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属客户
     */
    @TableField(fill = FieldFill.INSERT)
    private Long customerId;

    /**
     * 序号
     */
    private Integer reportId;

    /**
     * 科目编号
     */
    private String number;

    /**
     * 取数规则
     */
    private NumberRule numberRule;

    public enum NumberRule {
        AMOUNT(0, "实际发生金额"),
        BALANCE(1, "余额"),
        DEBIT_BALANCE(2, "借方余额"),
        CREDIT_BALANCE(3, "贷方余额"),
        SUBJECT_DEBIT_BALANCE(4, "科目借方余额"),
        SUBJECT_CREDIT_BALANCE(5, "科目贷方余额")
        ;

        @EnumValue
        private int value;
        private String label;
        NumberRule(int value, String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    public ReportFormula() {}

    public ReportFormula(Symbol symbol, String number, NumberRule numberRule) {
        this.symbol = symbol;
        this.number = number;
        this.numberRule = numberRule;
    }

}
