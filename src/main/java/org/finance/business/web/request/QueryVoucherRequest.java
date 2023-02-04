package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryVoucherRequest extends AbstractPageRequest {

    private Integer startPeriod;
    private Integer endPeriod;
    private CurrencyType currencyType;
    private Integer serialNumber;

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

}
