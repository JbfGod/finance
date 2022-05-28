package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

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
    private Long currencyId;
    private String currencyName;
    private BigDecimal rate;
    private String unit;
    @JsonIgnore
    private LocalDateTime voucherTime;
    private Integer attachmentNum;
    private List<Item> items;

    @Data
    public static class Item {

        private Long id;
        private String summary;
        private Long subjectId;
        private String subjectName;
        private LendingDirection lendingDirection;
        private BigDecimal amount;

    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getVoucherDate() {
        return voucherTime.toLocalDate();
    }
}
