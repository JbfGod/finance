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
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
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
            JwtAuthenticationToken token = new JwtAuthenticationToken(authorizationToken);
            token.setDetails(this.authenticationDetailsSource.buildDetails(request));
            Authentication authenticate = this.authenticationManager.authenticate(token);
            successfulAuthentication(authenticate);
            doFilter(request, response, filterChain);
        } catch (Exception e) {
            unsuccessfulAuthentication(response, e);
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

    private void unsuccessfulAuthentication(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(
                R.errorAndRedirect(MessageEnum.NO_AUTHENTICATION, exception.getMessage(), "")
        ));
        response.getWriter().flush();
    }

}
