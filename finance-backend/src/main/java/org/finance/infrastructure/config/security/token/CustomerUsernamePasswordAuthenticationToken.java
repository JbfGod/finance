package org.finance.infrastructure.config.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String customerAccount;

    public CustomerUsernamePasswordAuthenticationToken(String customerAccount, String username, String password) {
        super(username, password);
        this.customerAccount = customerAccount;
    }

    public CustomerUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    @Override
    public String getName() {
        return this.customerAccount;
    }

    public String getUsername() {
        Object principal = this.getPrincipal();
        return principal == null ? "" : principal.toString();
    }

    public String getCustomerAccount() {
        return customerAccount;
    }
}
