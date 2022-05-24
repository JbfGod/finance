package org.finance.infrastructure.config.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String customerNumber;

    public CustomerUsernamePasswordAuthenticationToken(String customerNumber, String account, String password) {
        super(account, password);
        this.customerNumber = customerNumber;
    }

    public CustomerUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    @Override
    public String getName() {
        return this.customerNumber;
    }

    public String getAccount() {
        Object principal = this.getPrincipal();
        return principal == null ? "" : principal.toString();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }
}
