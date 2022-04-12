package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jiangbangfa
 */
@Data
public class QuerySubjectRequest {

    @NotNull(message = "无法获取科目信息，请选先择所属行业")
    private Long industryId;
}
