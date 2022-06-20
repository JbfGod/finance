package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryOwnedCustomerRequest {

    private String customerNumber;
    private String customerName;

}
