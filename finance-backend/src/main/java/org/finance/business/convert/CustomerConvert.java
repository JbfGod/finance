package org.finance.business.convert;

import org.finance.business.entity.Customer;
import org.finance.business.web.vo.CustomerListVO;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
public interface CustomerConvert {

    CustomerConvert INSTANCE = Mappers.getMapper( CustomerConvert.class );

    CustomerListVO toCustomerListVO(Customer customer);
}
