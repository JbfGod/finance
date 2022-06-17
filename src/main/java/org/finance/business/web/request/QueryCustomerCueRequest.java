package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryCustomerCueRequest {

    public final static int DEFAULT_NUM = 5;
    private String keyword;
    private Integer num;

    public Integer getNum() {
        return this.num == null ? DEFAULT_NUM : this.num;
    }
}
