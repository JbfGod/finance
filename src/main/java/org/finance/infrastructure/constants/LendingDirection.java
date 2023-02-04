package org.finance.infrastructure.constants;

/**
 * 借贷方向
 * @author jiangbangfa
 */
public enum LendingDirection {

    /**
     * 借
     */
    BORROW,
    /**
     * 贷
     */
    LOAN,
    ;

    public boolean isLoan() {
        return this == LOAN;
    }

    public boolean isBorrow() {
        return this == BORROW;
    }
}
