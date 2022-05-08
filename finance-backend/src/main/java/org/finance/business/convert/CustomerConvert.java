package org.finance.business.convert;

import org.finance.business.entity.Customer;
import org.finance.business.web.request.AddCustomerRequest;
import org.finance.business.web.request.UpdateCustomerRequest;
import org.finance.business.web.vo.CustomerListVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface CustomerConvert {

    CustomerConvert INSTANCE = Mappers.getMapper( CustomerConvert.class );

    CustomerListVO toCustomerListVO(Customer customer);

    @Mapping(target = "userAccount", expression = "java(request.getUser().getAccount())")
    Customer toCustomer(AddCustomerRequest request);

    Customer toCustomer(UpdateCustomerRequest request);
}
