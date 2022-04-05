package org.finance.infrastructure.config.security.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.constants.MessageEnum;
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

/**
 * @author jiangbangfa
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AntPathRequestMatcher ignorePathMatcher;
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.ignorePathMatcher = new AntPathRequestMatcher(Constants.LOGIN_URL, "POST");
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requiresAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationToken = request.getHeader("Authorization").substring("Bearer ".length());
        JwtAuthenticationToken token = new JwtAuthenticationToken(authorizationToken);

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));

        try {
            Authentication authenticate = this.authenticationManager.authenticate(token);
            if (authenticate == null) {
                return;
            }
            successfulAuthentication(authenticate);
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

    protected void successfulAuthentication(Authentication authResult) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }

    private void unsuccessfulAuthentication(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(R.error(MessageEnum.NO_AUTHENTICATION, "无效的访问令牌, 请重新登录。")));
        response.getWriter().flush();
    }

}
