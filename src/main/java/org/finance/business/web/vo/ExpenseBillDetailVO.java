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
public class ExpenseBillDetailVO {

    private Long id;
    private String number;
    private String expensePerson;
    private String position;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expenseTime;
    private BigDecimal totalSubsidyAmount;
    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        private Integer serialNumber;
        private Long subjectId;
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
        private List<ItemSubsidy> subsidies;
        private List<ItemAttachment> attachments;

    }

    @Data
    public static class ItemSubsidy {
        private Long id;
        private Integer serialNumber;
        private Long subjectId;
        private String name;
        private Integer days;
        private BigDecimal amountForDay;
        private BigDecimal amount;

    }

    @Data
    public static class ItemAttachment {

        private Long id;
        private Integer serialNumber;
        private String name;
        private String url;
        private String remark;

    }
}
