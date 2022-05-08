package org.finance.business.convert;

import org.finance.business.entity.User;
import org.finance.business.web.request.AddUserRequest;
import org.finance.business.web.request.UpdateUserRequest;
import org.finance.business.web.vo.UserListVO;
import org.finance.business.web.vo.UserSelfVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper( UserConvert.class );

    UserSelfVO toUserSelfVO(User user);

    UserListVO toUserListVO(User user);

    @Mapping(target = "password", expression
            = "java(org.finance.infrastructure.config.security.util.SecurityUtil.encodePassword(req.getPassword()))")
    User toUser(AddUserRequest req, Long customerId, String customerAccount);

    @Mapping(target = "role", expression = "java(org.finance.business.entity.User.Role.ADMIN)")
    @Mapping(target = "password", expression
            = "java(org.finance.infrastructure.config.security.util.SecurityUtil.encodePassword(req.getPassword()))")
    User toAdminUser(AddUserRequest req);

    User toUser(UpdateUserRequest req);
}
