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

    private Long industryId;

    private Customer.Type type;

    private Customer.Status status;

    private String telephone;

    private String bankAccount;

    private String bankAccountName;

    private Boolean useForeignExchange;

}
