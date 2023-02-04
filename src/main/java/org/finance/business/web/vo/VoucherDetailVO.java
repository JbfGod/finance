package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.infrastructure.config.security.util.SecurityUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherDetailVO {

    private Long id;
    private Voucher.Source source;
    private Long expenseBillId;
    private Integer serialNumber;
    private Integer yearMonthNum;
    @JsonIgnore
    private AuditStatus auditStatus;
    private Long currencyId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer attachmentNum;
    private BigDecimal totalDebitAmount;
    private BigDecimal totalCreditAmount;
    private String creatorName;
    private String auditorName;
    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        private Integer serialNumber;
        private String summary;
        private Long subjectId;
        private String subjectName;
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
    }

    public Boolean getReviewed() {
        return this.auditStatus == AuditStatus.AUDITED;
    }

    public Boolean getClosed() {
        return this.yearMonthNum < SecurityUtil.getProxyCustomerCurrentPeriod();
    }
}
