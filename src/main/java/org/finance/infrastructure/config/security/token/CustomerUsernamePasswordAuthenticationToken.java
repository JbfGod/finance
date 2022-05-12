package org.finance.infrastructure.config.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String customerAccount;

    public CustomerUsernamePasswordAuthenticationToken(String customerAccount, String account, String password) {
        super(account, password);
        this.customerAccount = customerAccount;
    }

    public CustomerUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    @Override
    public String getName() {
        return this.customerAccount;
    }

    public String getAccount() {
        Object principal = this.getPrincipal();
        return principal == null ? "" : principal.toString();
    }

    public String getCustomerAccount() {
        return customerAccount;
    }
}
