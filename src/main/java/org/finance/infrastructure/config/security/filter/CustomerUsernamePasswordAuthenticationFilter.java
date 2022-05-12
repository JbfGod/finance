package org.finance.infrastructure.config.security.filter;

import com.alibaba.fastjson.JSON;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.token.CustomerUsernamePasswordAuthenticationToken;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.constants.MessageEnum;
import org.finance.infrastructure.util.CacheAttr;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    public CustomerUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate) {
        super(new AntPathRequestMatcher(Constants.LOGIN_URL, "POST"), authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //从json中获取username和password
        String customerAccount = null, account = null, password = null;
        customerAccount = request.getParameter("customerAccount");
        account = request.getParameter("account");
        password = request.getParameter("password");
        customerAccount = customerAccount == null ? "" : customerAccount;
        account = account == null ? "" : account;
        password = password == null ? "" : password;

        CustomerUsernamePasswordAuthenticationToken token = new CustomerUsernamePasswordAuthenticationToken(
                customerAccount, account, password
        );
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authResult;
        String token = jwtAuth.getJwt().getToken();

        // 将token写入缓存
        CacheAttr cacheAttr = CacheKeyUtil.getToken(token);
        redisTemplate.opsForValue().set(cacheAttr.getKey(), token, cacheAttr.getTimeout());

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(R.ok("Bearer " + token)));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(R.error(MessageEnum.NO_AUTHENTICATION, failed.getMessage())));
        response.getWriter().flush();
    }
}
