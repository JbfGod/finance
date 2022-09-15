package org.finance.business.web.request;

import lombok.Data;
import org.springframework.util.CollectionUtils;
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
public class UpdateExpenseBillRequest {

    @NotNull(message = "ID不能为空")
    private Long id;
    @NotBlank(message = "请填写报销人！")
    private String expensePerson;
    @NotBlank(message = "请填写职位！")
    private String position;
    @NotBlank(message = "请填写报销原因！")
    private String reason;
    @NotNull(message = "请填写报销时间！")
    private LocalDateTime expenseTime;

    private List<Long> deletedItemIds;
    private List<Long> deletedSubsidyIds;
    private List<Long> deletedAttachmentIds;

    @Valid
    @Size(min = 1, message = "至少添加报销项！")
    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        @NotNull(message = "请填写科目ID！")
        private Long subjectId;
        @NotBlank(message = "请选择科目！")
        private String subjectNumber;
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
        private String remark;
        @Valid
        private List<ItemSubsidy> subsidies;
        @Valid
        private List<ItemAttachment> attachments;

        public BigDecimal getSubsidyAmount() {
            if (CollectionUtils.isEmpty(subsidies)) {
                return new BigDecimal("0");
            }
            return subsidies.stream().reduce(new BigDecimal("0"),
                    (prev, curr) -> prev.add(curr.getAmount()),
                    BigDecimal::add);
        }

        public BigDecimal getSubtotalAmount() {
            return getSubsidyAmount().add(this.actualAmount);
        }
    }

    @Data
    public static class ItemSubsidy {
        private Long id;
        @NotNull(message = "请填写科目ID！")
        private Long subjectId;
        @NotBlank(message = "请选择科目！")
        private String subjectNumber;
        @NotNull(message = "请填写补助明细天数！")
        private Integer days;
        @NotNull(message = "请填写补助明细，元/天！")
        private BigDecimal amountForDay;
        @NotNull(message = "请填写补助金额！")
        private BigDecimal amount;
    }

    @Data
    public static class ItemAttachment {

        private Long id;
        @NotBlank(message = "请填写票据名称！")
        private String name;
        private MultipartFile file;
        private String remark;

    }
}
