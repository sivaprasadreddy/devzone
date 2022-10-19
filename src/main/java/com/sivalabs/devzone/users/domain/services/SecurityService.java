package com.sivalabs.devzone.users.domain.services;

import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.users.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService;

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            String username = securityUser.getUsername();
            return userService.getUserByEmail(username).orElse(null);
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.getUserByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
}
