package org.finance.business.web.request;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;
import org.finance.business.entity.VoucherItem;

/**
 * @author jiangbangfa
 */
@Data
public class QueryVoucherItemCueRequest {

    public final static int DEFAULT_NUM = 5;

    private String keyword;
    private Column column;
    private Integer num;

    public enum Column {
        SUMMARY(VoucherItem::getSummary);

        private final SFunction<VoucherItem, String> columnFunc;

        Column(SFunction<VoucherItem, String> columnFunc) {
            this.columnFunc = columnFunc;
        }

        public SFunction<VoucherItem, String> getColumnFunc() {
            return columnFunc;
        }
    }

    public Integer getNum() {
        return this.num == null ? DEFAULT_NUM : this.num;
    }
}
