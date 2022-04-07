package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryUserRequest extends AbstractPageRequest {

    private String name;
    private String customerAccount;
    private String account;

}
