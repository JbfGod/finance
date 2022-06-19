package org.finance.business.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface UserResourceConvert {

    UserResourceConvert INSTANCE = Mappers.getMapper( UserResourceConvert.class );

}
