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
import org.apache.commons.lang3.StringUtils;

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
 * @since 2022-10-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("profit_report")
public class ProfitReport implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Pattern formulaPattern = Pattern.compile("^(rowNum|subjectId):([\\d\\+\\-]+)");
    public static String formulaPartPattern = "(?<=[+-]\\w{1,20})\\b";

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属客户
     */
    @TableField(fill = FieldFill.INSERT)
    private Long customerId;

    private Integer serialNumber;

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
    private ProfitParam profitParam;

    @Data
    public static class ProfitParam {
        private BigDecimal annualAmount;
        private BigDecimal monthlyAmount;

        public ProfitParam() {
            BigDecimal defaultValue = new BigDecimal("0");
            this.annualAmount = defaultValue;
            this.monthlyAmount = defaultValue;
        }
    }


    public boolean validFormula() {
        if (StringUtils.isBlank(this.formula)) {
            return false;
        }
        Matcher matcher = formulaPattern.matcher(this.formula);
        return matcher.matches();
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

    public boolean isSubjectIdFormula() {
        return this.formula.startsWith("subjectId:");
    }

}
