package org.finance.infrastructure.config.security.provider;

import org.finance.business.entity.User;
import org.finance.business.entity.enums.ResourceModule;
import org.finance.infrastructure.common.UserRedisContextState;
import org.finance.infrastructure.config.security.CustomerUserService;
import org.finance.infrastructure.config.security.token.CustomerUsernamePasswordAuthenticationToken;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.config.security.util.JwtTokenUtil;
import org.finance.infrastructure.util.CacheAttr;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private final PasswordEncoder passwordEncoder;
    private final CustomerUserService customerUserService;
    private final RedisTemplate<String, UserRedisContextState> redisTemplate;
    private volatile String userNotFoundEncodedPassword;

    public CustomerUsernamePasswordAuthenticationProvider(CustomerUserService customerUserService, RedisTemplate<String, UserRedisContextState> redisTemplate) {
        this.redisTemplate = redisTemplate;
        super.hideUserNotFoundExceptions = false;
        this.customerUserService = customerUserService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(CustomerUsernamePasswordAuthenticationToken.class);
    }

    @Override
    protected final UserDetails retrieveUser(String customerId, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        prepareTimingAttackProtection();
        CustomerUsernamePasswordAuthenticationToken token = (CustomerUsernamePasswordAuthenticationToken) authentication;
        try {
            UserDetails loadedUser = this.customerUserService.loadUserByCustomerAndAccount(customerId, token.getAccount());
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "CustomerUserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        User dbUser = (User) principal;
        // 生成访问令牌
        String token = JwtTokenUtil.generateTokenByUser(dbUser.getId());
        // 查询权限
        /*List<Resource> resources = this.customerUserService.loadAuthoritiesByUserId(dbUser.getId());
        List<GrantedAuthority> authorities = resources
                .stream().flatMap(ResourceConvert.INSTANCE::toAccess)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(String.format("%s%s", Constants.ROLE_PREFIX, dbUser.getRole())));*/
        // 构造Authentication
        JwtAuthenticationToken result = new JwtAuthenticationToken(token);
        result.setDetails(dbUser);
        // 将token写入缓存
        CacheAttr cacheAttr = CacheKeyUtil.getToken(token);
        UserRedisContextState state = new UserRedisContextState().setModule(ResourceModule.MANAGE);
        redisTemplate.opsForValue().set(cacheAttr.getKey(), state, cacheAttr.getTimeout());
        return result;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException("用户名或密码错误");
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

}
