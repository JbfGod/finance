package org.finance.business.web.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author jiangbangfa
 */
@Data
public class QueryVoucherRequest extends AbstractPageRequest {

    private LocalDate startDate;
    private LocalDate endDate;
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

    public CurrencyType getCurrencyType() {
        return currencyType == null ? CurrencyType.LOCAL: currencyType;
    }
}
