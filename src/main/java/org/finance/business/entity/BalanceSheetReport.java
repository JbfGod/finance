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
 * 利润报表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-11-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("balance_sheet_report")
public class BalanceSheetReport implements Serializable {

    private static final long serialVersionUID = 1L;
    public static Pattern formulaPattern = Pattern.compile("^(rowNum|subjectId):([\\d\\+\\-]+)");
    public static String formulaPartPattern = "(?<=[+-]\\w{1,20})\\b";
    public static String ASSETS_TYPE = "assets";
    public static String EQUITY_TYPE = "equity";

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
     * 资产名称
     */
    private String assetsName;

    /**
     * 资产行次
     */
    private Integer assetsRowNumber;

    /**
     * 资产公式
     */
    private String assetsFormula;

    /**
     * 权益名称
     */
    private String equityName;

    /**
     * 权益行次
     */
    private Integer equityRowNumber;

    /**
     * 权益公式
     */
    private String equityFormula;

    @TableField(exist = false)
    private Row profitParam;

    @Data
    public static class Row {
        private Integer rowNumber;
        private BigDecimal openingAmount;
        private BigDecimal closingAmount;

        public Row() {
            BigDecimal defaultValue = new BigDecimal("0");
            this.openingAmount = defaultValue;
            this.closingAmount = defaultValue;
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

    public static boolean isSubjectIdFormula(String formula) {
        return formula.startsWith("subjectId:");
    }

}
