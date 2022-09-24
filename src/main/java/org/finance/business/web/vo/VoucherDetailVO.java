package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.business.entity.Voucher;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Long currencyId;
    private String currencyName;
    private BigDecimal rate;
    private String unit;
    @JsonIgnore
    private LocalDate voucherDate;
    private Integer attachmentNum;
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

}
