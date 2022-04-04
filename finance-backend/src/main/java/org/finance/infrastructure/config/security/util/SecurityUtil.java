package org.finance.infrastructure.config.security.util;

import org.finance.business.entity.User;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author jiangbangfa
 */
public class SecurityUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        throw new AuthenticationServiceException("找不到当前上下文用户失败");
    }

    public static User getCurrentUserId () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

}
