package org.finance.business.web.vo;

import lombok.Data;
import org.finance.infrastructure.constants.LendingDirection;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class VoucherBookVO {

    private Long id;
    private String subjectNumber;
    private String subjectName;
    private Integer yearMonthNum;
    private Integer serialNumber;
    private String summary;
    private LendingDirection lendingDirection;
    private BigDecimal rate;
    private BigDecimal amount;

}
