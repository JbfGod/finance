package org.finance.infrastructure.config.security.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.finance.business.convert.ResourceConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Resource;
import org.finance.business.entity.User;
import org.finance.infrastructure.common.UserRedisContextState;
import org.finance.infrastructure.config.security.CustomerUserService;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.config.security.util.JwtTokenUtil;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.util.CacheAttr;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomerUserService customerUserService;
    private final RedisTemplate<String, UserRedisContextState> redisTemplate;

    public JwtAuthenticationProvider(CustomerUserService customerUserService, RedisTemplate<String, UserRedisContextState> redisTemplate) {
        this.customerUserService = customerUserService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;

        User dbUser = null;
        try {
            DecodedJWT jwt = JwtTokenUtil.getDecodedJWT(token.getJwt());
            String cacheKey = CacheKeyUtil.getToken(jwt.getToken()).getKey();
            UserRedisContextState state = redisTemplate.opsForValue().get(cacheKey);
            if (state == null) {
                throw new BadCredentialsException("无效的凭证，请重新登录！");
            }
            String userId = jwt.getSubject();
            dbUser = customerUserService.loadUserById(Long.valueOf(userId));
            if (dbUser == null) {
                throw new AccountNotFoundException("账户不存在！");
            }
            // 如果当前用户代理客户单位，则该单位信息当如当前用户上下文中
            Long proxyCustomerId = state.getProxyCustomerId();
            if (proxyCustomerId != null) {
                dbUser.setProxyCustomer(customerUserService.loadCustomerById(proxyCustomerId));
            }
        } catch (Exception e) {
            throw new BadCredentialsException("无效的凭证，请重新登录！", e);
        }

        // 刷新登录凭证
        this.flushToken(token.getJwt());
        return new UsernamePasswordAuthenticationToken(dbUser, token.getCredentials(), grantedAuthorities(dbUser));
     }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

    /**
     * 授权
     */
    private List<GrantedAuthority> grantedAuthorities(User user) {
        List<Resource> resources = this.customerUserService.loadAuthoritiesByUserId(user.getId());
        user.setResources(resources);
        List<GrantedAuthority> authorities = resources.stream()
                .flatMap(ResourceConvert.INSTANCE::toAccess)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(String.format("%s%s", Constants.ROLE_PREFIX, user.getRole())));
        // 获取当前操作员记账的客户单位
        Customer customer = Optional.ofNullable(user.getProxyCustomer()).orElse(user.getCustomer());

        // 添加认证标识权限
        authorities.add(new SimpleGrantedAuthority("isAuth"));
        // 是否能够添加外币凭证
        if (Objects.equals(customer.getUseForeignExchange(), true)) {
            authorities.add(new SimpleGrantedAuthority("voucher:addForeign"));
        }
        return authorities;
    }

    private void flushToken(String jwt) {
        CacheAttr cacheAttr = CacheKeyUtil.getToken(jwt);
        Long expire = redisTemplate.getExpire(cacheAttr.getKey(), TimeUnit.MINUTES);
        if (expire == null || expire < 15) {
            // 续签一下jwt
            redisTemplate.expire(cacheAttr.getKey(), cacheAttr.getTimeout());
        }
    }
}
