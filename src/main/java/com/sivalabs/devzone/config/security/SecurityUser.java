package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.domain.entities.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public SecurityUser(User user) {
        super(user.getEmail(), user.getPassword(),
            user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
