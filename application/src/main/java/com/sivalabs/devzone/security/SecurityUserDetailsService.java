package com.sivalabs.devzone.security;

import com.sivalabs.devzone.users.application.data.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository
                .findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        "No user found with username " + username));
    }
}
