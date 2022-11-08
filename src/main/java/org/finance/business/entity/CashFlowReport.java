package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 现金流量报表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-11-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cash_flow_report")
public class CashFlowReport implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Pattern formulaPattern = Pattern.compile("^(rowNum|voucherItemId):([\\d\\+\\-]+)");
    public static String formulaPartPattern = "(?<=[+-]\\w{1,20})\\b";

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 序号
     */
    private Integer serialNumber;

    /**
     * 所属客户
     */
    @TableField(fill = FieldFill.INSERT)
    private Long customerId;

    /**
     * 月份
     */
    private Integer yearMonthNum;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 行次
     */
    private Integer rowNumber;

    /**
     * 计算公式
     */
    private String formula;

    @TableField(exist = false)
    private Row row;

    @Data
    public static class Row {
        private BigDecimal annualAmount;
        private BigDecimal monthlyAmount;

        public Row() {
            BigDecimal defaultValue = new BigDecimal("0");
            this.annualAmount = defaultValue;
            this.monthlyAmount = defaultValue;
        }
    }

    public static List<String> splitFormula(String formula) {
        Matcher matcher = formulaPattern.matcher(formula);
        if (matcher.matches()) {
            return Arrays.asList(matcher.group(2).split(formulaPartPattern));
        }
        return Collections.emptyList();
    }

    public static boolean isRowNumFormula(String formula) {
        return formula.startsWith("rowNum:");
    }

    public boolean isVoucherItemIdFormula() {
        return this.formula.startsWith("voucherItemId:");
    }

}
