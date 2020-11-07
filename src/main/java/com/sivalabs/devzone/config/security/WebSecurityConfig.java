package com.sivalabs.devzone.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        private final TokenAuthenticationFilter tokenAuthenticationFilter;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                        .antMatchers("/api/auth/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/users/change-password").authenticated()
                        .antMatchers("/api/users/**").permitAll()
                        // .antMatchers(HttpMethod.POST,"/users").hasAnyRole("USER", "ADMIN")
                        // .anyRequest().authenticated()
                        .and()
                    .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);

            http.csrf()
                // .ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                .disable().headers().frameOptions().sameOrigin() // allow use of frame to same origin urls
            ;
        }

    }

    @Configuration
    @Order(2)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) {
            web.ignoring()
                .antMatchers("/static/**", "/js/**", "/css/**", "/images/**", "/favicon.ico", "/h2-console/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/resources/**", "/webjars/**").permitAll()
                    .antMatchers("/registration", "/forgot-password", "/reset-password").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    // .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error").permitAll()
                        .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();
        }

    }

}
