package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryCustomerCueRequest {

    private String keyword;
    private Integer num;

}
