package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    /**
     * 期初金额(借)
     */
    private BigDecimal debitOpeningAmount;

    /**
     * 期初金额(贷)
     */
    private BigDecimal creditOpeningAmount;

    /**
     * 期末金额(借)
     */
    private BigDecimal debitClosingAmount;

    /**
     * 期末金额(贷)
     */
    private BigDecimal creditClosingAmount;

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

    public static AccountBalance newInstance() {
        BigDecimal zero = new BigDecimal("0");
        return new AccountBalance()
                .setDebitOpeningAmount(zero)
                .setDebitClosingAmount(zero)
                .setDebitCurrentAmount(zero)
                .setDebitAnnualAmount(zero)
                .setCreditOpeningAmount(zero)
                .setCreditClosingAmount(zero)
                .setCreditCurrentAmount(zero)
                .setCreditAnnualAmount(zero);
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

        return this.setDebitOpeningAmount(accountBalance.getDebitClosingAmount())
                .setCreditOpeningAmount(accountBalance.getCreditClosingAmount())
                .calcClosingBalance(accountBalance.getDebitClosingAmount(), accountBalance.getCreditClosingAmount());
    }

    public AccountBalance calcOpeningBalance(BigDecimal debitAmount, BigDecimal creditAmount) {
        BigDecimal balance = this.debitOpeningAmount.subtract(this.creditOpeningAmount)
                .add(debitAmount.subtract(creditAmount));
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            return this.setDebitOpeningAmount(balance);
        }
        return this.setCreditOpeningAmount(balance.abs());
    }

    public AccountBalance calcClosingBalance(BigDecimal debitAmount, BigDecimal creditAmount) {
        BigDecimal balance = this.debitClosingAmount.subtract(this.creditClosingAmount)
                .add(debitAmount.subtract(creditAmount));
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            return this.setDebitClosingAmount(balance);
        }
        return this.setCreditClosingAmount(balance.abs());
    }
}
