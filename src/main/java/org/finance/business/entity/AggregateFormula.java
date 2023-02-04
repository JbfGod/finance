package org.finance.business.entity;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报表条目计算公式(条目)
 * </p>
 *
 * @author jiangbangfa
 * @since 2023-01-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("aggregate_formula")
public class AggregateFormula extends AbstractFormula implements Serializable {

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
     * 条目
     */
    private Integer aggregateReportId;

    /**
     * 参与计算的条目id
     */
    private Integer reportId;

    public AggregateFormula() {
    }

    public AggregateFormula(Symbol symbol, Integer reportId) {
        this.symbol = symbol;
        this.reportId = reportId;
    }

    public static List<AggregateFormula> sumList(Integer...reportIds) {
        return Arrays.stream(reportIds).map(num -> new AggregateFormula(Symbol.ADD, num))
                .collect(Collectors.toList());
    }
}
