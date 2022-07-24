package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.enums.AuditStatus;

import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
@Data
public class InitialBalanceVO {

    private Long id;

    private Integer yearMonthNum;
    private Boolean bookkeeping;
    private AuditStatus auditStatus;
    private String creatorName;

    public YearMonth getYearMonthDate() {
        return YearMonth.of(yearMonthNum / 100, yearMonthNum % 100);
    }
}
