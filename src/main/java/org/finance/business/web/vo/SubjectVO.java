package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.Subject;
import org.finance.infrastructure.constants.LendingDirection;

import java.math.BigDecimal;

/**
 * @author jiangbangfa
 */
@Data
public class SubjectVO {

    private Long id;
    private String number;
    private Long industryId;
    private String industry;
    private String name;
    private Subject.Category category;
    private LendingDirection lendingDirection;
    private Subject.AssistSettlement assistSettlement;
    private Integer level;
    private Boolean hasLeaf;
    private String parentNumber;
    private Long parentId;
    private String remark;

    private BigDecimal beginningBalance;
    private BigDecimal openingBalance;
    private BigDecimal debitAnnualAmount;
    private BigDecimal creditAnnualAmount;

    private BigDecimal balance;
}
