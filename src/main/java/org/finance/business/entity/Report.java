package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.finance.business.entity.enums.Symbol;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报表条目
 * </p>
 *
 * @author jiangbangfa
 * @since 2023-01-20
 */
@Getter
@Setter
@Accessors(chain = true)
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 所属客户
     */
    private Long customerId;

    private Category category;

    private String name;

    private Integer level;

    private Integer rowNum;

    public final static int REPORT_FORMULA = 1;
    public final static int AGGREGATE_FORMULA = 2;
    public final static int TOTAL_FORMULA = 3;
    /**
     * 0:none, 1:report_formula, 2:aggregate_formula
     */
    private Integer formulaType;
    @TableField(exist = false)
    private List<ReportFormula> reportFormulas;
    @TableField(exist = false)
    private List<AggregateFormula> aggregateFormulas;

    @TableField(exist = false)
    private BigDecimal beginBalance;
    @TableField(exist = false)
    private BigDecimal endBalance;
    @TableField(exist = false)
    private BigDecimal annualAmount;
    @TableField(exist = false)
    private BigDecimal currentPeriodAmount;
    @TableField(exist = false)
    private Boolean flag;

    public enum Category {
        /**
         * 资产负债表
         */
        ASSETS(1, "资产负债表", 1, 100),
        /**
         * 利润表
         */
        PROFIT(2, "利润表", 101, 200),

        CASH_FLOW(3, "现金流量表", 201, 300)
        ;
        @EnumValue
        private int value;
        private String label;
        private int minId;
        private int maxId;

        Category(int value, String label, int minId, int maxId) {
            this.value = value;
            this.label = label;
            this.minId = minId;
            this.maxId = maxId;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public int getMinId() {
            return minId;
        }

        public int getMaxId() {
            return maxId;
        }
    }

    public Report() {
    }

    public Report(Integer id, Category category, String name, Integer rowNum, Integer level, Integer formulaType) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.rowNum = rowNum;
        this.level = level;
        this.formulaType = formulaType;
    }

    public Report buildBalanceFormulas(String...numbers) {
        this.reportFormulas = Arrays.stream(numbers).map(num -> new ReportFormula(Symbol.ADD, num, ReportFormula.NumberRule.BALANCE).setReportId(this.id))
                .collect(Collectors.toList());
        return this;
    }

    public Report buildAmountFormulas(String...numbers) {
        this.reportFormulas = Arrays.stream(numbers).map(num -> new ReportFormula(Symbol.ADD, num, ReportFormula.NumberRule.AMOUNT).setReportId(this.id))
                .collect(Collectors.toList());
        return this;
    }

    public Report buildMinusBalanceFormulas(String number1, String number2) {
        this.reportFormulas = Arrays.asList(
                new ReportFormula(Symbol.ADD, number1, ReportFormula.NumberRule.BALANCE).setReportId(this.id),
                new ReportFormula(Symbol.MINUS, number2, ReportFormula.NumberRule.BALANCE).setReportId(this.id)
        );
        return this;
    }

    public Report buildAggregateFormulas(Integer...reportIds) {
        this.aggregateFormulas = Arrays.stream(reportIds).map(reportId -> new AggregateFormula(Symbol.ADD, reportId).setAggregateReportId(this.id))
                .collect(Collectors.toList());
        return this;
    }
}
