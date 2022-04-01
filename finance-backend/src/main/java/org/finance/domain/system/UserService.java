package org.finance.domain.system;

import org.finance.domain.system.mapper.UserMapper;
import org.finance.infrastructure.config.security.CustomerUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jiangbangfa
 */
@Service
public class UserService implements CustomerUserDetailService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByCustomerIdAndUsername(String customerId, String username) throws UsernameNotFoundException {
        return null;
    }
}
