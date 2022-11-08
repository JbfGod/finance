package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * 科目余额（月关帐的时候生成）
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_balance")
public class AccountBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Long customerId;

    /**
     * 年份:yyyy
     */
    private Integer year;

    /**
     * 月份:yyyyMM
     */
    private Integer yearMonthNum;

    private Long subjectId;

    private String subjectNumber;

    private String currency;

    private Integer maxVoucherNumber;

    /**
     * 期初余额(原币)
     */
    private BigDecimal openingBalance;

    /**
     * 期初余额(本币)
     */
    private BigDecimal localOpeningBalance;

    /**
     * 期末余额(原币)
     */
    private BigDecimal closingBalance;

    /**
     * 期末余额(本币)
     */
    private BigDecimal localClosingBalance;

    /**
     * 本期金额(借)
     */
    private BigDecimal debitCurrentAmount;

    /**
     * 本期金额(贷)
     */
    private BigDecimal creditCurrentAmount;

    /**
     * 年累计金额(借)
     */
    private BigDecimal debitAnnualAmount;

    /**
     * 年累计金额(贷)
     */
    private BigDecimal creditAnnualAmount;

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

    public static AccountBalance newInstance() {
        BigDecimal zero = new BigDecimal("0");
        return new AccountBalance()
                .setCurrency("人民币")
                .setOpeningBalance(zero)
                .setLocalOpeningBalance(zero)
                .setDebitCurrentAmount(zero)
                .setDebitAnnualAmount(zero)
                .setClosingBalance(zero)
                .setLocalClosingBalance(zero)
                .setCreditCurrentAmount(zero)
                .setCreditAnnualAmount(zero);
    }

    public static AccountBalance newInstance(Subject subject) {
        return newInstance()
                .setSubjectId(subject.getId())
                .setSubjectNumber(subject.getNumber());
    }

    public AccountBalance mergeLastPeriod(AccountBalance accountBalance) {
        if (accountBalance == null) {
            return this;
        }
        // 若是同一年则累积年金额
        if (Objects.equals(this.year, accountBalance.getYear())) {
            this.setDebitAnnualAmount(this.debitCurrentAmount.add(accountBalance.getDebitAnnualAmount()))
                .setCreditAnnualAmount(this.creditCurrentAmount.add(accountBalance.getCreditAnnualAmount()));
        }

        return this.setOpeningBalance(accountBalance.getClosingBalance())
                .setLocalOpeningBalance(accountBalance.getLocalClosingBalance())
                .calcClosingBalance(accountBalance.getClosingBalance(), accountBalance.getLocalClosingBalance());
    }

    public AccountBalance calcOpeningBalance(BigDecimal openingBalance, BigDecimal localOpeningBalance) {
        this.setOpeningBalance(openingBalance.add(this.openingBalance));
        return this.setLocalOpeningBalance(localOpeningBalance.add(this.localOpeningBalance));
    }

    public AccountBalance calcClosingBalance(BigDecimal closingBalance, BigDecimal localClosingBalance) {
        this.setClosingBalance(closingBalance.add(this.closingBalance));
        return this.setLocalClosingBalance(localClosingBalance.add(this.localClosingBalance));
    }

    public BigDecimal getDebitOpeningAmount() {
        return this.openingBalance.compareTo(BigDecimal.ZERO) > 0? this.openingBalance : BigDecimal.ZERO;
    }

    public BigDecimal getCreditOpeningAmount() {
        return this.openingBalance.compareTo(BigDecimal.ZERO) < 0? this.openingBalance.abs() : BigDecimal.ZERO;
    }

    public BigDecimal getDebitClosingAmount() {
        return this.closingBalance.compareTo(BigDecimal.ZERO) > 0? this.closingBalance : BigDecimal.ZERO;
    }

    public BigDecimal getCreditClosingAmount() {
        return this.closingBalance.compareTo(BigDecimal.ZERO) < 0? this.closingBalance.abs() : BigDecimal.ZERO;
    }

    public LendingDirection getOpeningBalanceLendingDirection() {
        int direction = this.openingBalance.compareTo(BigDecimal.ZERO);
        return direction > 0 ? LendingDirection.DEBIT
                : direction == 0 ? LendingDirection.BALANCE
                : LendingDirection.CREDIT;
    }

    public LendingDirection getClosingBalanceLendingDirection() {
        int direction = this.closingBalance.compareTo(BigDecimal.ZERO);
        return direction > 0 ? LendingDirection.DEBIT
                : direction == 0 ? LendingDirection.BALANCE
                : LendingDirection.CREDIT;
    }

    public String getKey() {
        return String.format("%s-%s", this.currency, this.subjectId);
    }

    public BigDecimal getCurrentAmount() {
        return this.debitCurrentAmount.subtract(this.creditCurrentAmount);
    }

    public BigDecimal getAnnualAmount() {
        return this.debitAnnualAmount.subtract(this.creditAnnualAmount);
    }
}
