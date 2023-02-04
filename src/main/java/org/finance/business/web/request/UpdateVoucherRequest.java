package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Currency;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateVoucherRequest {

    private final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    @NotNull(message = "ID不能为空！")
    private Long id;

    private Integer serialNumber;

    private Long currencyId;
    @NotNull(message = "请输入凭证时间！")
    private LocalDate voucherDate;
    private Integer attachmentNum;
    @Valid
    @Size(min = 1, message = "至少添加一条凭证记录！")
    private List<Item> items;

    private List<Long> deletedItemIds;

    @Data
    public static class Item {

        private Long id;
        private String summary;
        @NotNull(message = "请选择科目！")
        private Long subjectId;
        @NotBlank(message = "请选择科目！")
        private String subjectNumber;
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
        public BigDecimal getDebitAmount() {
            return this.debitAmount == null? BigDecimal.ZERO: this.debitAmount;
        }

        public BigDecimal getCreditAmount() {
            return this.creditAmount == null? BigDecimal.ZERO: this.creditAmount;
        }
    }

    public Integer getYear() {
        return voucherDate.getYear();
    }

    public Integer getYearMonthNum() {
        return Integer.parseInt(voucherDate.format(YEAR_MONTH_FMT));
    }

    public BigDecimal getTotalDebitAmount() {
        return this.items.stream().reduce(new BigDecimal("0"),
                (curr, next) -> curr.add(next.getDebitAmount()),
                BigDecimal::add
        );
    }

    public BigDecimal getTotalCreditAmount() {
        return this.items.stream().reduce(new BigDecimal("0"),
                (curr, next) -> curr.add(next.getCreditAmount()),
                BigDecimal::add
        );
    }

    public BigDecimal getTotalLocalDebitAmount() {
        return this.getTotalDebitAmount();
    }

    public BigDecimal getTotalLocalCreditAmount() {
        return this.getTotalCreditAmount();
    }

    public Integer getAttachmentNum() {
        return this.attachmentNum == null ? 0 : this.attachmentNum;
    }


    public Long getCurrencyId() {
        return currencyId == null? Currency.LOCAL_CURRENCY.getId() : currencyId;
    }

}
