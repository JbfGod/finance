package org.finance.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.finance.business.web.request.UpdateExpenseBillRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        @JsonIgnore
        private Long subjectId;
        @JsonIgnore
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

        public Map<String, Object> getSubject() {
            Map<String, Object> map = new HashMap<>();
            map.put("label", this.name);
            map.put("value", this.subjectId);
            return map;
        }

    }

    @Data
    public static class ItemSubsidy {
        private Long id;
        @JsonIgnore
        private Long subjectId;
        @JsonIgnore
        private String name;
        private Integer days;
        private BigDecimal amountForDay;
        private BigDecimal amount;

        public Map<String, Object> getSubject() {
            Map<String, Object> map = new HashMap<>();
            map.put("label", this.name);
            map.put("value", this.subjectId);
            return map;
        }
    }

    @Data
    public static class ItemAttachment {

        private Long id;
        private String name;
        private String url;
        private String remark;

    }
}
