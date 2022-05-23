package org.finance.business.web.request;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;
import org.finance.business.entity.ExpenseBill;

/**
 * @author jiangbangfa
 */
@Data
public class QueryExpenseBillCueRequest {

    private String keyword;
    private Column column;
    private Integer num;

    public enum Column {
        REASON(ExpenseBill::getReason),
        ;
        private final SFunction<ExpenseBill, String> columnFunc;

        Column(SFunction<ExpenseBill, String> columnFunc) {
            this.columnFunc = columnFunc;
        }

        public SFunction<ExpenseBill, String> getColumnFunc() {
            return columnFunc;
        }
    }

}
