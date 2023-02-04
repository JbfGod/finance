package org.finance.business.web.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class AuditingVoucherRequest {

    @Size(message = "请至少选择一条凭证数据")
    private List<Long> ids;

}
