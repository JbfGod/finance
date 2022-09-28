package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private LendingDirection lendingDirection;
    private BigDecimal balance;

    public enum LendingDirection {
        /**
         * 借
         */
        DEBIT("借"),
        /**
         * 平
         */
        BALANCE("平"),
        /**
         * 贷
         */
        CREDIT("贷")
        ;

        private String label;
        LendingDirection(String label) {
            this.label = label;
        }

        @JsonValue
        public String getLabel() {
            return this.label;
        }
    }

    public String getId() {
        return UUID.randomUUID().toString();
    }

    public SubLedgerVO setBalance(BigDecimal balance) {
        int direction = balance.compareTo(BigDecimal.ZERO);
        this.balance = direction > 0 ? balance : balance.abs();
        this.lendingDirection = direction > 0 ? LendingDirection.DEBIT
                : direction == 0 ? LendingDirection.BALANCE
                : LendingDirection.CREDIT;
        return this;
    }
}
