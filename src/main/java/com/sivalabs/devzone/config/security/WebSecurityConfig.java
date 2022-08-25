package com.sivalabs.devzone.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final RoleHierarchyImpl roleHierarchy;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.antMatcher("/**")
                .authorizeRequests()
                .expressionHandler(webExpressionHandler(roleHierarchy))
                .antMatchers(
                        "/webjars/**",
                        "/resources/**",
                        "/static/**",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/favicon.ico",
                        "/h2-console/**")
                .permitAll()
                .antMatchers("/", "/login", "/registration")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .and()
                .build();
    }

    private static SecurityExpressionHandler<FilterInvocation> webExpressionHandler(
            RoleHierarchy roleHierarchy) {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler =
                new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultWebSecurityExpressionHandler;
    }
}
