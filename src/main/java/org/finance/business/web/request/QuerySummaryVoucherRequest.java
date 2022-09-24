package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class QuerySummaryVoucherRequest {

    @NotNull(message = "yearMonth 不能为空！")
    private YearMonth yearMonth;
    @NotNull(message = "科目ID不能为空！")
    private Long subjectId;

}
