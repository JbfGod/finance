package org.finance.business.web.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class ExpenseItemVO {

    private Long billId;
    private Integer serialNumber;
    private Long subjectId;
    private String subject;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String travelPlace;
    private String summary;
    private Integer numOfBill;
    private BigDecimal billAmount;
    private BigDecimal actualAmount;
    private BigDecimal subsidyAmount;
    private BigDecimal subtotalAmount;
    private String remark;

}
