package org.finance.infrastructure.config.security;

import org.finance.business.service.UserService;
import org.finance.infrastructure.config.security.filter.CustomerUsernamePasswordAuthenticationFilter;
import org.finance.infrastructure.config.security.filter.JwtAuthenticationFilter;
import org.finance.infrastructure.config.security.filter.OptionsRequestFilter;
import org.finance.infrastructure.config.security.handler.MyPermissionEvaluator;
import org.finance.infrastructure.config.security.provider.CustomerUsernamePasswordAuthenticationProvider;
import org.finance.infrastructure.config.security.provider.JwtAuthenticationProvider;
import org.finance.infrastructure.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author jiangbangfa
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserLogoutHandler userLogoutHandler;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, Constants.LOGIN_URL).permitAll()
                .antMatchers("/favicon.ico", "/doc.html", "/swagger.json"
                        ,"/druid/**", "/api/downloadOpenapi"
                        , "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/swagger-resources/**", "/v2/api-docs")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().disable()
                .cors()
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                        new Header("Access-control-Allow-Origin", "*"),
                        new Header("Access-Control-Expose-Headers", "Authorization,CustomerId")
                )))
                .and()
                .addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class)
                .addFilterAt(customerUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout().logoutUrl("/api/logout").addLogoutHandler(userLogoutHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customerUsernamePasswordAuthenticationProvider());
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomerUsernamePasswordAuthenticationFilter customerUsernamePasswordAuthenticationFilter() throws Exception {
        return new CustomerUsernamePasswordAuthenticationFilter(authenticationManagerBean(), redisTemplate);
    }

    @Bean
    public CustomerUsernamePasswordAuthenticationProvider customerUsernamePasswordAuthenticationProvider() {
        return new CustomerUsernamePasswordAuthenticationProvider(userService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManagerBean(), redisTemplate);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() throws Exception {
        return new JwtAuthenticationProvider(userService, redisTemplate);
    }

    @Bean
    public MyPermissionEvaluator myPermissionEvaluator() {
        return new MyPermissionEvaluator();
    }
}
