package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.business.entity.Currency;
import org.finance.business.web.request.AbstractPageRequest;

import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherVO extends AbstractPageRequest {

    private Long id;
    private String customerNumber;
    private String yearMonthNum;
    private String currencyName;
    private String unit;
    private Integer serialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime voucherTime;
    private Integer attachmentNum;
    private String auditStatus;
    private String creatorName;
    @JsonIgnore
    private Long currencyId;
    public Currency.Type getCurrencyType() {
        return Currency.LOCAL_CURRENCY.getId().equals(currencyId) ? Currency.Type.LOCAL : Currency.Type.FOREIGN;
    }

}
