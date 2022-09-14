package org.finance.business.web.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.Currency;
import org.finance.infrastructure.constants.LendingDirection;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @NotNull(message = "请选择币别！")
    private Long currencyId;
    @NotNull(message = "币别名称不能为空！")
    private String currencyName;
    @NotNull(message = "汇率不能为空！")
    private BigDecimal rate;
    @NotBlank(message = "请输入单位！")
    private String unit;
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
        @NotBlank(message = "请输入摘要！")
        private String summary;
        @NotNull(message = "请选择科目！")
        private Long subjectId;
        @NotBlank(message = "请选择科目！")
        private String subjectNumber;
        @NotNull(message = "请选择借贷方向！")
        private LendingDirection lendingDirection;
        @NotNull(message = "请输入借方金额！")
        private BigDecimal amount;

    }

    public Integer getYear() {
        return voucherDate.getYear();
    }

    public Integer getYearMonthNum() {
        return Integer.parseInt(voucherDate.format(YEAR_MONTH_FMT));
    }

    public BigDecimal getTotalCurrencyAmount() {
        return this.items.stream().reduce(new BigDecimal("0"),
                (curr, next) -> curr.add(next.getAmount()),
                BigDecimal::add
        );
    }

    public BigDecimal getTotalLocalCurrencyAmount() {
        return this.getTotalCurrencyAmount().multiply(this.getRate());
    }

    public Integer getAttachmentNum() {
        return this.attachmentNum == null ? 0 : this.attachmentNum;
    }

    public String getCurrencyName() {
        return StringUtils.isBlank(currencyName) ? Currency.LOCAL_CURRENCY.getName() : currencyName;
    }

    public BigDecimal getRate() {
        return rate == null ? Currency.LOCAL_CURRENCY.getRate() : rate;
    }

    public Long getCurrencyId() {
        return currencyId == null? Currency.LOCAL_CURRENCY.getId() : currencyId;
    }

    public LocalDateTime getVoucherTime() {
        return voucherDate.atStartOfDay();
    }
}
