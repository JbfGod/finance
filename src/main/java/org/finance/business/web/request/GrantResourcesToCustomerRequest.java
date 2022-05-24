package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class GrantResourcesToCustomerRequest {

    @NotNull(message = "请填写客户ID")
    private Long customerId;

    @Size(min = 1, message = "至少选择一个功能权限")
    private List<Long> resourceIds;
}