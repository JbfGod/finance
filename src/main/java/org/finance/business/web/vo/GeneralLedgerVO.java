package org.finance.business.web.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.business.entity.AccountBalance;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
@Builder
@Accessors(chain = true)
public class GeneralLedgerVO {

    private Long subjectId;
    private String subjectNumber;
    private String subjectName;
    private Integer month;
    private Integer maxVoucherNumber;
    private String summary;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private AccountBalance.LendingDirection lendingDirection;
    private BigDecimal balance;
    private Integer rowSpan;


    public String getId() {
        return this.subjectId + this.summary + this.month;
    }

    public GeneralLedgerVO setBalance(BigDecimal balance) {
        int direction = balance.compareTo(BigDecimal.ZERO);
        this.balance = direction > 0 ? balance : balance.abs();
        this.lendingDirection = direction > 0 ? AccountBalance.LendingDirection.DEBIT
                : direction == 0 ? AccountBalance.LendingDirection.BALANCE
                : AccountBalance.LendingDirection.CREDIT;
        return this;
    }
}
