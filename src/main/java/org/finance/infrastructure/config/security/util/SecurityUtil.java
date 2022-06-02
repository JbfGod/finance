package org.finance.infrastructure.config.security.util;

import org.finance.business.entity.Resource;
import org.finance.business.entity.User;
import org.finance.infrastructure.constants.Constants;
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

    public static User getCurrentUserOfNullable() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getCustomerId() {
        return getCurrentUser().getCustomerId();
    }

    public static String getCustomerNumber() {
        return getCurrentUser().getCustomerNumber();
    }

    public static Long getUserId() {
        return getCurrentUser().getId();
    }

    public static String getUserName() {
        return getCurrentUser().getName();
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

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(
                        String.format("%s%s", Constants.ROLE_PREFIX, role)
                ));
    }

    public static boolean canViewAll(String target) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(
                        String.format("%s:%s:view:all", Resource.Type.DATA_SCOPE.name(), target)
                ));
    }
}
