package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.business.entity.AccountBalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class SubLedgerVO {

    private Long subjectId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;
    private Integer voucherNumber;
    private Integer serialNumber;
    private String summary;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private AccountBalance.LendingDirection lendingDirection;
    private BigDecimal balance;

    public String getId() {
        return UUID.randomUUID().toString();
    }

    public SubLedgerVO setBalance(BigDecimal balance) {
        int direction = balance.compareTo(BigDecimal.ZERO);
        this.balance = direction > 0 ? balance : balance.abs();
        this.lendingDirection = direction > 0 ? AccountBalance.LendingDirection.DEBIT
                : direction == 0 ? AccountBalance.LendingDirection.BALANCE
                : AccountBalance.LendingDirection.CREDIT;
        return this;
    }
}