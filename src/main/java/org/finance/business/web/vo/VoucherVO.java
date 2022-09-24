package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.business.entity.Currency;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.web.request.AbstractPageRequest;

import java.time.LocalDate;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherVO extends AbstractPageRequest {

    private Long id;
    private Voucher.Source source;
    private Long expenseBillId;
    private String customerNumber;
    private String yearMonthNum;
    private String currencyName;
    private String unit;
    private Integer serialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;
    private Integer attachmentNum;
    private Boolean bookkeeping;
    private AuditStatus auditStatus;
    private String creatorName;
    @JsonIgnore
    private Long currencyId;
    public Currency.Type getCurrencyType() {
        return Currency.LOCAL_CURRENCY.getId().equals(currencyId) ? Currency.Type.LOCAL : Currency.Type.FOREIGN;
    }

}
