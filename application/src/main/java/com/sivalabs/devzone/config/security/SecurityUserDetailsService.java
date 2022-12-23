package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.users.domain.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService
                .getUserByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        "No user found with username " + username));
    }
}
