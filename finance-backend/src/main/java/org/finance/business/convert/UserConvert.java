package org.finance.business.convert;

import org.finance.business.entity.User;
import org.finance.business.web.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper( UserConvert.class );

    UserVO toUserVO(User user);
}
