package com.sivalabs.devzone.security;

import com.sivalabs.devzone.users.application.data.repository.UserRepository;
import com.sivalabs.devzone.users.domain.model.User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityService {
    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            String username = securityUser.getUsername();
            return userRepository.findByEmail(username).orElse(null);
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
}
