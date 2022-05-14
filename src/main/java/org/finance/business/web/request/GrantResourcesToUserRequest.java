package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class GrantResourcesToUserRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Size(min = 1, message = "至少选择一个功能权限")
    private List<Long> resourceIds;
}
