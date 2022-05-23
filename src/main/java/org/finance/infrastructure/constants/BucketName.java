package org.finance.infrastructure.constants;

/**
 * @author jiangbangfa
 */
public enum BucketName {

    /**
     * 费用报销单
     */
    EXPENSE_BILL("expense-bill")
    ;

    private String name;

    BucketName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
