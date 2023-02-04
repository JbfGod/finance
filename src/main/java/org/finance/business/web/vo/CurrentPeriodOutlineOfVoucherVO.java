package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class CurrentPeriodOutlineOfVoucherVO {

    /**
     * 当期时间yyyyMM
     */
    private Integer yearMonthNum;
    /**
     * 当期凭证数量
     */
    private Long voucherTotal;

}
