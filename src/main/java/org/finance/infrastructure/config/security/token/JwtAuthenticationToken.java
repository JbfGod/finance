package org.finance.infrastructure.config.security.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jiangbangfa
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private DecodedJWT jwt;

    public JwtAuthenticationToken(DecodedJWT jwt) {
        super(new ArrayList<>());
        this.jwt = jwt;
    }

    public JwtAuthenticationToken(DecodedJWT jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return this.jwt;
    }

    public DecodedJWT getJwt() {
        return jwt;
    }
}
