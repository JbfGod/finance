package org.finance.infrastructure.config.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jiangbangfa
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;

    public JwtAuthenticationToken(String jwt) {
        super(new ArrayList<>());
        this.jwt = jwt;
    }

    public JwtAuthenticationToken(String jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
    }

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return this.jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
