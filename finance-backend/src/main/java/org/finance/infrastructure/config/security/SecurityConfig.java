package org.finance.infrastructure.config.security;

import org.apache.catalina.filters.CorsFilter;
import org.finance.infrastructure.config.security.filter.CustomerUsernamePasswordAuthenticationFilter;
import org.finance.infrastructure.config.security.filter.JwtAuthenticationFilter;
import org.finance.infrastructure.config.security.filter.OptionsRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.Arrays;

/**
 * @author jiangbangfa
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/config", "/actuator/**", "/api/health", "/api/user/apply",
                "/wx/user/info", "/wx/user/login", "/wx/user/phone", "/wx/news", "/wx/category","/wx/org",
                "/wx/user/visitor", "/wx/user/registerOfficer", "/api/zhongjian/idaas/**");
        web.ignoring().antMatchers(HttpMethod.OPTIONS);
        web.ignoring().antMatchers("/","/csrf");
        web.ignoring().antMatchers("/favicon.ico");
        web.ignoring().antMatchers(HttpMethod.GET, "/doc.html")
                .antMatchers(HttpMethod.GET, "/swagger-ui/**")
                .antMatchers(HttpMethod.GET, "/webjars/**")
                .antMatchers(HttpMethod.GET, "/swagger-resources/**")
                .antMatchers(HttpMethod.GET, "/v2/api-docs");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().disable()
                .formLogin().disable()
                .cors()
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                    new Header("Access-control-Allow-Origin","*"),
                    new Header("Access-Control-Expose-Headers","Authorization")
                )))
                .and()
                .addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(), LogoutFilter.class)
                .addFilterAt(new CustomerUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
