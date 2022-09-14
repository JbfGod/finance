package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.finance.infrastructure.constants.LendingDirection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 凭证账簿
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("voucher_book")
public class VoucherBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 年份:yyyy
     */
    private Integer year;

    /**
     * 月份:yyyyMM
     */
    private Integer yearMonthNum;

    private Long voucherId;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目编号
     */
    private String subjectNumber;

    /**
     * 凭证日期
     */
    private LocalDateTime voucherTime;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 借贷方向
     */
    private LendingDirection lendingDirection;

    /**
     * 原币ID
     */
    private Long currencyId;

    /**
     * 原币名称
     */
    private String currencyName;

    /**
     * 原币汇率
     */
    private BigDecimal rate;

    /**
     * 金额
     */
    private BigDecimal amount;


}
