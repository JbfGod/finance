package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.finance.business.entity.enums.AuditStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author jiangbangfa
 */
@Data
public class ExpenseBillVO {

    @NotNull(message = "ID不能为空")
    private Long id;
    private String number;
    private Long customerId;
    private String customerNumber;
    private String expensePerson;
    private String position;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expenseTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    private AuditStatus auditStatus;

}
