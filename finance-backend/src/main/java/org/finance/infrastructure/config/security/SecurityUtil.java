package org.finance.infrastructure.config.security;

import org.finance.business.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author jiangbangfa
 */
public class SecurityUtil {

    public static User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

}
