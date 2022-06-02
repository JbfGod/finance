package org.finance.infrastructure.config.security.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.config.security.util.JwtTokenUtil;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.constants.MessageEnum;
import org.finance.infrastructure.util.CacheAttr;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangbangfa
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AntPathRequestMatcher ignorePathMatcher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ignorePathMatcher = new AntPathRequestMatcher(Constants.LOGIN_URL, "POST");
        this.authenticationManager = authenticationManager;
        this.authenticationDetailsSource = new WebAuthenticationDetailsSource();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requiresAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationToken = request.getHeader("Authorization").substring("Bearer ".length());
        try {
            DecodedJWT jwt = JwtTokenUtil.getDecodedJWT(authorizationToken);
            JwtAuthenticationToken token = new JwtAuthenticationToken(jwt);
            token.setDetails(this.authenticationDetailsSource.buildDetails(request));
            Authentication authenticate = this.authenticationManager.authenticate(token);
            if (authenticate == null) {
                return;
            }
            successfulAuthentication(jwt, authenticate);
            doFilter(request, response, filterChain);
        } catch (Exception e) {
            log.info("JWT Invalid", e);
            unsuccessfulAuthentication(response);
        }
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return !ignorePathMatcher.matcher(request).isMatch() && authHeader != null && authHeader.startsWith("Bearer ");
    }

    protected void successfulAuthentication(DecodedJWT jwt, Authentication authResult) {
        CacheAttr cacheAttr = CacheKeyUtil.getToken(jwt.getToken());
        Long expire = redisTemplate.getExpire(cacheAttr.getKey(), TimeUnit.MINUTES);
        if (expire == null || expire < 15) {
            // 续签一下jwt
            redisTemplate.expire(cacheAttr.getKey(), cacheAttr.getTimeout());
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }

    private void unsuccessfulAuthentication(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(
                R.errorAndRedirect(MessageEnum.NO_AUTHENTICATION, "无效的访问令牌, 请重新登录。", "")
        ));
        response.getWriter().flush();
    }

}
