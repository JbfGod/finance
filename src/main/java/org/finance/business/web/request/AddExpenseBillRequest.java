package org.finance.business.web.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class AddExpenseBillRequest {

    private String number;
    @NotBlank(message = "请填写报销人！")
    private String expensePerson;
    @NotBlank(message = "请填写职位！")
    private String position;
    @NotBlank(message = "请填写报销原因！")
    private String reason;
    @NotNull(message = "请填写报销时间！")
    private LocalDateTime expenseTime;
    @NotNull(message = "请填写合计补助金额！")
    private BigDecimal totalSubsidyAmount;

    @Valid
    @NotNull(message = "至少添加一项报销项！")
    @Size(min = 1, message = "至少添加一项报销项！")
    private List<Item> items;

    @Data
    public static class Item {
        @NotNull(message = "请填写科目ID！")
        private Long subjectId;
        @NotNull(message = "请填写费用名称！")
        private String name;
        @NotNull(message = "请填写开始时间！")
        private LocalDateTime beginTime;
        @NotNull(message = "请填写结束时间！")
        private LocalDateTime endTime;
        @NotBlank(message = "请填写出差起讫地点！")
        private String travelPlace;
        @NotBlank(message = "请填写摘要！")
        private String summary;
        @NotNull(message = "请填写票据张数！")
        private Integer numOfBill;
        @NotNull(message = "请填写票据金额！")
        private BigDecimal billAmount;
        @NotNull(message = "请填写实报金额！")
        private BigDecimal actualAmount;
        @NotNull(message = "请填写补助费用金额！")
        private BigDecimal subsidyAmount;
        @NotNull(message = "请填小计金额！")
        private BigDecimal subtotalAmount;
        private String remark;
        @Valid
        private List<ItemSubsidy> subsidies;
        @Valid
        private List<ItemAttachment> attachments;
    }

    @Data
    public static class ItemSubsidy {
        @NotNull(message = "请填写科目ID！")
        private Long subjectId;
        @NotNull(message = "请填写补助费用名称！")
        private String name;
        @NotNull(message = "请填写补助明细天数！")
        private Integer days;
        @NotNull(message = "请填写补助明细，元/天！")
        private BigDecimal amountForDay;
        @NotNull(message = "请填写补助金额！")
        private BigDecimal amount;
    }

    @Data
    public static class ItemAttachment {

        @NotBlank(message = "请填写票据名称！")
        private String name;
        @NotNull
        private MultipartFile file;
        private String remark;

    }
}
