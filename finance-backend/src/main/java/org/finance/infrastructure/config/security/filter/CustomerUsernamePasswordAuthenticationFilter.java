package org.finance.infrastructure.config.security.filter;

import com.alibaba.fastjson.JSON;
import org.finance.infrastructure.config.security.token.CustomerUsernamePasswordAuthenticationToken;
import org.finance.infrastructure.config.security.token.JwtAuthenticationToken;
import org.finance.infrastructure.constants.Constants;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangbangfa
 */
public class CustomerUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public CustomerUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(Constants.LOGIN_URL, "POST"), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //从json中获取username和password
        String customerAccount = null, username = null, password = null;
        customerAccount = request.getParameter("customerAccount");
        username = request.getParameter("username");
        password = request.getParameter("password");
        customerAccount = customerAccount == null ? "" : customerAccount;
        username = username == null ? "" : username;
        password = password == null ? "" : password;

        CustomerUsernamePasswordAuthenticationToken token = new CustomerUsernamePasswordAuthenticationToken(
                customerAccount, username, password
        );
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authResult;
        response.setHeader("Authorization", "Bearer " + token.getJwt());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        Map<String, String> map = new HashMap<>(5);
        map.put("msg", "用户名或密码错误");
        response.getWriter().print(JSON.toJSONString(map));
        response.getWriter().flush();
    }
}
