package org.finance.infrastructure.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author jiangbangfa
 */
public interface CustomerUserDetailService {

    /**
     * 根据客户编号和用户名查询用户信息
     * @param customerId
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByCustomerIdAndUsername(String customerId, String username) throws UsernameNotFoundException;

}
