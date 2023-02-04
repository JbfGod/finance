package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Subject;

/**
 * @author jiangbangfa
 */
@Data
public class QuerySubjectRequest extends AbstractPageRequest {

    private Long industryId;
    private String name;
    private String number;
    private Subject.Category category;

}
