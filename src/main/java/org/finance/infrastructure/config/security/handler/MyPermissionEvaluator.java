package org.finance.infrastructure.config.security.handler;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
public class MyPermissionEvaluator implements PermissionEvaluator  {

    public final static String PERMIT_PREFIX = "PERMIT:";

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return hasPermissionName(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

    private boolean hasPermissionName(Authentication authentication, Object targetDomainObject, Object permission) {
        Set<String> permissionNames = getAuthoritySet(authentication);
        for (String permissionName : permissionNames) {
            if (permissionName.equals(String.format("%s:%s", targetDomainObject, permission))) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getAuthoritySet(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
