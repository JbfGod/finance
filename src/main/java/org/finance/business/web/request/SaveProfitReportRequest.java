package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class SaveProfitReportRequest {

    @NotNull(message = "请选择月份")
    private Integer yearMonthNum;
    @NotNull(message = "至少添加一条数据")
    @Size(min = 1, message = "至少添加一条数据")
    private List<Row> rows;

    @Data
    public static class Row {
        private String name;
        private Integer rowNum;
        private String formula;
    }
}
