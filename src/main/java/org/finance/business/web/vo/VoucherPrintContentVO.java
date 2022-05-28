package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherPrintContentVO {

    private final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    private Long id;
    private String customerName;
    private Long currencyId;
    private String currencyName;
    private String unit;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime voucherTime;
    private Integer attachmentNum;
    private BigDecimal totalCurrencyAmount;
    private BigDecimal totalLocalCurrencyAmount;
    private Integer serialNumber;
    private List<Item> items;

    @Data
    public static class Item {

        private Long id;
        private String summary;
        private Long subjectId;
        private String subjectName;
        private LendingDirection lendingDirection;
        private BigDecimal rate;
        private BigDecimal amount;

        public BigDecimal getLocalAmount() {
            return rate.multiply(amount);
        }

    }

}
