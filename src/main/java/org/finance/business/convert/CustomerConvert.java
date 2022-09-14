package org.finance.business.convert;

import org.finance.business.entity.Customer;
import org.finance.business.web.request.AddCustomerRequest;
import org.finance.business.web.request.UpdateCustomerRequest;
import org.finance.business.web.vo.CustomerCueVO;
import org.finance.business.web.vo.CustomerListVO;
import org.finance.business.web.vo.OwnedApprovalCustomerVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface CustomerConvert {

    CustomerConvert INSTANCE = Mappers.getMapper( CustomerConvert.class );

    CustomerListVO toCustomerListVO(Customer customer);

    Customer toCustomer(AddCustomerRequest request);

    Customer toCustomer(UpdateCustomerRequest request);

    CustomerCueVO toCustomerCueVO(Customer customer);

    OwnedApprovalCustomerVO toOwnedApprovalCustomerVO(Customer customer);
}
