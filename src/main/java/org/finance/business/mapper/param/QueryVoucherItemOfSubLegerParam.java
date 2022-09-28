package org.finance.business.mapper.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class QueryVoucherItemOfSubLegerParam extends AbstractSubjectParam {

    private Integer yearMonthNum;
    private String currencyName;

}
