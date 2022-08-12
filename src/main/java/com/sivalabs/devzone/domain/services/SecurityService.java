package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.entities.RoleEnum;
import com.sivalabs.devzone.domain.entities.User;
import java.util.Arrays;
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
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser.getUser();
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.getUserByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }

    public Long loginUserId() {
        User loginUser = loginUser();
        if (loginUser != null) {
            return loginUser.getId();
        }
        return null;
    }

    public boolean isCurrentUserAdmin() {
        return isUserHasAnyRole(loginUser(), RoleEnum.ROLE_ADMIN);
    }

    public boolean isUserAdminOrModerator(User loginUser) {
        return isUserHasAnyRole(loginUser, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR);
    }

    private boolean isUserHasAnyRole(User loginUser, RoleEnum... roles) {
        return Arrays.asList(roles).contains(loginUser.getRole());
    }
}
