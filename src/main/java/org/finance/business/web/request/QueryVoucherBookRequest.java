package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QueryVoucherBookRequest extends AbstractPageRequest {

    private Integer yearMonthNum;

}
