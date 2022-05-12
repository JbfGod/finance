package org.finance.business.web.request;

import lombok.Data;
import org.finance.business.entity.Subject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author jiangbangfa
 */
@Data
public class AddSubjectRequest {

    @NotBlank(message = "科目编号不能为空！")
    @Size(min = 2, max = 20, message = "科目编号只允许有2-20个字符！")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "科目编号只允许包含数字和字母")
    private String number;

    @NotBlank(message = "科目名称不能为空！")
    @Size(min = 2, max = 20, message = "科目名称只允许有2-20个字符！")
    private String name;

    @NotNull(message = "请选择所属行业")
    private Long industryId;

    @NotNull(message = "请选择科目类型")
    private Subject.Type type;

    private Subject.Direction direction;

    private Subject.AssistSettlement assistSettlement;

    @NotNull(message = "父级ID不能为空")
    private Long parentId;

    @Size(max = 255, message = "备注信息不能超出255个字符")
    private String remark;
}
