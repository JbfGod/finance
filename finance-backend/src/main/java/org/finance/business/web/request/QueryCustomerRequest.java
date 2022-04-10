package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Customer;

/**
 * @author jiangbangfa
 */
@Data
public class QueryCustomerRequest extends AbstractPageRequest {

    private String userAccount;

    private String account;

    private String name;

    private Long categoryId;

    private Customer.Type type;

    private Boolean enabled;

    private String telephone;

    private String bankAccount;

    private String bankAccountName;

    private Boolean useForeignExchange;

}
