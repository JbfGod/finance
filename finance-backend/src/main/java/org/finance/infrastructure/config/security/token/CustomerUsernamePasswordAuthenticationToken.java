package org.finance.infrastructure.config.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String customerId;

    public CustomerUsernamePasswordAuthenticationToken(String customerId, String username, String password) {
        super(username, password);
        this.customerId = customerId;
    }

    public CustomerUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    @Override
    public String getName() {
        return this.customerId;
    }

    public String getUsername() {
        Object principal = this.getPrincipal();
        return principal == null ? "" : principal.toString();
    }

}
