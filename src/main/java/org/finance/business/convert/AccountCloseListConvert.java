package org.finance.business.convert;

import org.finance.business.entity.AccountCloseList;
import org.finance.business.web.request.CloseAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */

@Mapper
public interface AccountCloseListConvert {

    AccountCloseListConvert INSTANCE = Mappers.getMapper( AccountCloseListConvert.class );

    AccountCloseList toAccountCloseList(CloseAccountRequest request);
}
