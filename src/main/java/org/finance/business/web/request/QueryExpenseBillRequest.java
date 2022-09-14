package org.finance.business.web.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author jiangbangfa
 */
@Data
public class QueryExpenseBillRequest extends AbstractPageRequest {

    private Long customerId;
    private String number;
    private LocalDate startDate;
    private LocalDate endDate;
    private AuditStatus auditStatus;

    public enum AuditStatus {
        AUDITED, APPROVED
    }
}
