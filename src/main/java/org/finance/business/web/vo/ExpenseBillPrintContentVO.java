package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class ExpenseBillPrintContentVO {

    private Long id;
    private String number;
    private String expensePerson;
    private String position;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expenseTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    private String creatorName;
    private BigDecimal totalSubsidyAmount;
    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        private String name;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime beginTime;
        @JsonFormat(pattern = "yyyy-MM-dd")
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
}
