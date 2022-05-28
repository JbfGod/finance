package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryVoucherRequest extends AbstractPageRequest {

    private Integer yearMonthNum;
    private CurrencyType currencyType;

    public enum CurrencyType {
        /**
         * 外币凭证
         */
        FOREIGN,
        /**
         * 本币凭证
         */
        LOCAL
        ;
    }

    public CurrencyType getCurrencyType() {
        return currencyType == null ? CurrencyType.LOCAL: currencyType;
    }
}
