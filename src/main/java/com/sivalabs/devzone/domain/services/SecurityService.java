package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.entities.RoleEnum;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.LinkDTO;
import java.util.Arrays;
import java.util.Objects;
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

    public boolean canCurrentUserEditLink(LinkDTO linkDTO) {
        User loginUser = loginUser();
        return loginUser != null
                && linkDTO != null
                && (Objects.equals(linkDTO.getCreatedUserId(), loginUser.getId())
                        || isCurrentUserAdminOrModerator(loginUser));
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

    private boolean isCurrentUserAdminOrModerator(User loginUser) {
        return isUserHasAnyRole(loginUser, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR);
    }

    private boolean isUserHasAnyRole(User loginUser, RoleEnum... roles) {
        return Arrays.asList(roles).contains(loginUser.getRole());
    }
}
