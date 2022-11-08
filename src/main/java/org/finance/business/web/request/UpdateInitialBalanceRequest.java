package org.finance.business.web.request;

import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateInitialBalanceRequest {

    private final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    @NotNull(message = "ID不能为空！")
    private Long id;
    @NotNull(message = "币别名称不能为空！")
    private String currencyName;
    @NotNull(message = "请选择科目！")
    private Long subjectId;
    @NotBlank(message = "请选择科目！")
    private String subjectNumber;
    @NotNull(message = "请选择借贷方向！")
    private LendingDirection lendingDirection;
    @NotNull(message = "请输入原币金额！")
    private BigDecimal amount;
    @NotNull(message = "请输入本币金额！")
    private BigDecimal localAmount;

    public BigDecimal getDebitAmount() {
        return lendingDirection == LendingDirection.BORROW? amount : new BigDecimal("0");
    }

    public BigDecimal getCreditAmount() {
        return lendingDirection == LendingDirection.LOAN? amount : new BigDecimal("0");
    }

    public BigDecimal getLocalDebitAmount() {
        return lendingDirection == LendingDirection.BORROW? localAmount : new BigDecimal("0");
    }

    public BigDecimal getLocalCreditAmount() {
        return lendingDirection == LendingDirection.LOAN? localAmount : new BigDecimal("0");
    }
}
