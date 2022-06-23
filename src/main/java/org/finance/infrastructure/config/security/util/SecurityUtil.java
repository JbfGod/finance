package org.finance.infrastructure.config.security.util;

import org.finance.business.entity.Customer;
import org.finance.business.entity.User;
import org.finance.infrastructure.constants.Constants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
public class SecurityUtil {

    public final static BCryptPasswordEncoder PWD_ENCODER = new BCryptPasswordEncoder();

    public static List<String> getPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        throw new AuthenticationServiceException("找不到当前上下文用户失败");
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static User getCurrentUserOfNullable() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public static boolean isSuperCustomer() {
        return Customer.DEFAULT_ID.equals(getCurrentUser().getCustomerId());
    }

    public static boolean isSuperAdmin() {
        User currentUser = getCurrentUser();
        return Customer.DEFAULT_ID.equals(currentUser.getCustomerId()) && currentUser.getRole() == User.Role.ADMIN;
    }

    public static Customer getProxyCustomer() {
        User currentUser = getCurrentUser();
        return Optional.ofNullable(currentUser.getProxyCustomer()).orElse(currentUser.getCustomer());
    }

    public static Long getProxyCustomerId() {
        return getProxyCustomer().getId();
    }

    public static String getProxyCustomerNumber() {
        return getProxyCustomer().getNumber();
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
                        String.format("%s:view:all", target)
                ));
    }
}
