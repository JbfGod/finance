package org.finance.infrastructure.config.security;

import org.finance.business.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author jiangbangfa
 */
public interface CustomerUserService {

    /**
     * 根据客户编号和用户名查询用户信息
     * @param customerAccount
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    User loadUserByCustomerAndAccount(String customerAccount, String account) throws UsernameNotFoundException;

    /**
     * @param userId
     * @return
     */
    User loadUserById(Long userId);

    /**
     * 查询用户拥有的权限
     * @param userId
     * @return
     */
    List<GrantedAuthority> loadAuthoritiesByUserId(Long userId);
}
