package com.sivalabs.devzone.config;

import com.sivalabs.devzone.users.domain.model.RoleEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(
                        "/webjars/**",
                        "/resources/**",
                        "/static/**",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/favicon.ico",
                        "/h2-console/**")
                .permitAll()
                .requestMatchers("/", "/login", "/registration", "/posts", "/api/categories")
                .permitAll()
                .anyRequest()
                .permitAll();

        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return web -> web.expressionHandler(webSecurityExpressionHandler());
    }

    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(RoleEnum.getRoleHierarchy());
        logger.debug("RoleHierarchy: {}", RoleEnum.getRoleHierarchy());
        return roleHierarchy;
    }

    DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler =
                new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
