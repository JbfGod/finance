package org.finance.business.convert;

import javax.annotation.Generated;
import org.finance.business.entity.User;
import org.finance.business.web.request.AddUserRequest;
import org.finance.business.web.request.UpdateUserRequest;
import org.finance.business.web.vo.UserListVO;
import org.finance.business.web.vo.UserSelfVO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-11T19:12:16+0800",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.1.jar, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class UserConvertImpl implements UserConvert {

    @Override
    public UserSelfVO toUserSelfVO(User user) {
        if ( user == null ) {
            return null;
        }

        UserSelfVO userSelfVO = new UserSelfVO();

        userSelfVO.setId( user.getId() );
        userSelfVO.setCustomerId( user.getCustomerId() );
        userSelfVO.setName( user.getName() );
        userSelfVO.setCustomerAccount( user.getCustomerAccount() );
        userSelfVO.setAccount( user.getAccount() );
        userSelfVO.setRole( user.getRole() );
        userSelfVO.setCreateBy( user.getCreateBy() );
        userSelfVO.setCreateTime( user.getCreateTime() );
        userSelfVO.setModifyBy( user.getModifyBy() );
        userSelfVO.setModifyTime( user.getModifyTime() );

        return userSelfVO;
    }

    @Override
    public UserListVO toUserListVO(User user) {
        if ( user == null ) {
            return null;
        }

        UserListVO userListVO = new UserListVO();

        userListVO.setId( user.getId() );
        userListVO.setCustomerId( user.getCustomerId() );
        userListVO.setName( user.getName() );
        userListVO.setCustomerAccount( user.getCustomerAccount() );
        userListVO.setAccount( user.getAccount() );
        userListVO.setCreateBy( user.getCreateBy() );
        userListVO.setCreateTime( user.getCreateTime() );
        userListVO.setModifyBy( user.getModifyBy() );
        userListVO.setModifyTime( user.getModifyTime() );

        return userListVO;
    }

    @Override
    public User toUser(AddUserRequest req, Long customerId, String customerAccount) {
        if ( req == null && customerId == null && customerAccount == null ) {
            return null;
        }

        User user = new User();

        if ( req != null ) {
            user.setName( req.getName() );
            user.setAccount( req.getAccount() );
        }
        if ( customerId != null ) {
            user.setCustomerId( customerId );
        }
        if ( customerAccount != null ) {
            user.setCustomerAccount( customerAccount );
        }
        user.setRole( org.finance.business.entity.User.Role.NORMAL );
        user.setPassword( org.finance.infrastructure.config.security.util.SecurityUtil.encodePassword(req.getPassword()) );

        return user;
    }

    @Override
    public User toUser(UpdateUserRequest req) {
        if ( req == null ) {
            return null;
        }

        User user = new User();

        user.setId( req.getId() );
        user.setName( req.getName() );

        return user;
    }
}
