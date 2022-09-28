package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private LendingDirection lendingDirection;
    private BigDecimal balance;
    private Integer rowSpan;


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
        return this.subjectId + this.summary + this.month;
    }

    public GeneralLedgerVO setBalance(BigDecimal balance) {
        int direction = balance.compareTo(BigDecimal.ZERO);
        this.balance = direction > 0 ? balance : balance.abs();
        this.lendingDirection = direction > 0 ? LendingDirection.DEBIT
                : direction == 0 ? LendingDirection.BALANCE
                : LendingDirection.CREDIT;
        return this;
    }
}
