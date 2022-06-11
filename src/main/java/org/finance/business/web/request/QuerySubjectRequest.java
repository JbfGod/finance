package org.finance.business.web.request;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class QuerySubjectRequest {

    private Long industryId;
    private String name;
    private String number;
}
