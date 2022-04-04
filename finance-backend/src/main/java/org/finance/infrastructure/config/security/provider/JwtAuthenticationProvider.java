package org.finance.infrastructure.config.security.provider;

import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.CustomerUserService;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.config.security.util.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.security.auth.login.AccountNotFoundException;

/**
 * @author jiangbangfa
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomerUserService userService;

    public JwtAuthenticationProvider(CustomerUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        User user = null;
        try {
            String userId = JwtTokenUtil.getUserIdAndVerifyByToken(token.getJwt());
            user = userService.loadUserById(Long.valueOf(userId));
            if (user == null) {
                throw new AccountNotFoundException();
            }
        } catch (Exception e) {
            throw new BadCredentialsException("JWT verify fail", e);
        }
        return new UsernamePasswordAuthenticationToken(user, token.getCredentials(), userService.loadAuthoritiesByUserId(user.getId()));
     }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
