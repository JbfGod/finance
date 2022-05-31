package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherBookVO {

    private Long id;
    private String subjectNumber;
    private String subjectName;
    private Integer yearMonthNum;
    private Integer serialNumber;
    private String summary;
    private LendingDirection lendingDirection;
    @JsonIgnore
    private BigDecimal rate;
    @JsonIgnore
    private BigDecimal amount;

    public BigDecimal getDebitAmount() {
        return lendingDirection == LendingDirection.BORROW ? rate.multiply(amount) : null;
    }

    public BigDecimal getCreditAmount() {
        return lendingDirection == LendingDirection.LOAN ? rate.multiply(amount) : null;
    }
}
