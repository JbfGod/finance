package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Subject;
import org.finance.infrastructure.constants.LendingDirection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class UpdateSubjectRequest {

    @NotNull(message = "请填写科目ID")
    private Long id;

    @NotBlank(message = "请填写科目名称！")
    @Size(min = 2, max = 20, message = "科目名称只允许有2-20个字符！")
    private String name;

    @NotNull(message = "请填写科目类别！")
    private Subject.Category category;

    private LendingDirection lendingDirection;

    private Subject.AssistSettlement assistSettlement;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;

}
