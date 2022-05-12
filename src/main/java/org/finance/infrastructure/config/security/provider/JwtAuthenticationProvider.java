package org.finance.infrastructure.config.security.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.CustomerUserService;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

/**
 * @author jiangbangfa
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomerUserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtAuthenticationProvider(CustomerUserService userService, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;

        User dbUser = null;
        try {
            DecodedJWT jwt = token.getJwt();
            String cacheKey = CacheKeyUtil.getToken(jwt.getToken()).getKey();
            Boolean hasKey = redisTemplate.hasKey(cacheKey);
            if (Boolean.FALSE.equals(hasKey)) {
                throw new BadCredentialsException("JWT verify fail");
            }
            String userId = jwt.getSubject();
            dbUser = userService.loadUserById(Long.valueOf(userId));
            if (dbUser == null) {
                throw new AccountNotFoundException();
            }
        } catch (Exception e) {
            throw new BadCredentialsException("JWT verify fail", e);
        }
        List<GrantedAuthority> authorities = this.userService.loadAuthoritiesByUserId(dbUser.getId());
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", dbUser.getRole())));
        return new UsernamePasswordAuthenticationToken(dbUser, token.getCredentials(), authorities);
     }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
