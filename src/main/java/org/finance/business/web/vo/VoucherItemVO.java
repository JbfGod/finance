package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherItemVO {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;
    private Long voucherId;
    private Integer voucherNumber;
    private String summary;
    private BigDecimal localDebitAmount;
    private BigDecimal localCreditAmount;

}
