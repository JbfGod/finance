package org.finance.infrastructure.config.security.util;

import org.finance.business.entity.User;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jiangbangfa
 */
public class SecurityUtil {

    public final static BCryptPasswordEncoder PWD_ENCODER = new BCryptPasswordEncoder();

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        throw new AuthenticationServiceException("找不到当前上下文用户失败");
    }

    public static Long getCurrentUserId () {
        return getCurrentUser().getId();
    }

    public static String encodePassword(String pwd) {
        return PWD_ENCODER.encode(pwd);
    }

    public boolean matchPassword(String pwd1, String pwd2) {
        return PWD_ENCODER.matches(pwd1, pwd2);
    }
}
