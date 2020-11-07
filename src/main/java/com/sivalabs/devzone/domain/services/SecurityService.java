package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.sivalabs.devzone.domain.utils.AppConstants.ROLE_ADMIN;
import static com.sivalabs.devzone.domain.utils.AppConstants.ROLE_MODERATOR;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser.getUser();
        }
        return null;
    }

    public boolean canCurrentUserEditLink(LinkDTO linkDTO) {
        User loginUser = loginUser();
        return loginUser != null && linkDTO != null
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
        return isUserHasAnyRole(loginUser(), ROLE_ADMIN);
    }

    private boolean isCurrentUserAdminOrModerator(User loginUser) {
        return isUserHasAnyRole(loginUser, ROLE_ADMIN, ROLE_MODERATOR);
    }

    private boolean isUserHasAnyRole(User loginUser, String... roles) {
        List<String> roleList = Arrays.asList(roles);
        if (loginUser != null && loginUser.getRoles() != null) {
            return loginUser.getRoles().stream()
                .anyMatch(role -> roleList.contains(role.getName()));
        }
        return false;
    }
}
