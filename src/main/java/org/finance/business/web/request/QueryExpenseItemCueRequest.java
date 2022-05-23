package org.finance.business.web.request;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;
import org.finance.business.entity.ExpenseBill;
import org.finance.business.entity.ExpenseItem;

/**
 * @author jiangbangfa
 */
@Data
public class QueryExpenseItemCueRequest {

    private String keyword;
    private Column column;
    private Integer num;

    public enum Column {
        TRAVEL_PLACE(ExpenseItem::getTravelPlace),
        SUMMARY(ExpenseItem::getSummary),
        REMARK(ExpenseItem::getRemark),
        ;
        private final SFunction<ExpenseItem, String> columnFunc;

        Column(SFunction<ExpenseItem, String> columnFunc) {
            this.columnFunc = columnFunc;
        }

        public SFunction<ExpenseItem, String> getColumnFunc() {
            return columnFunc;
        }
    }
}
