package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.domain.entities.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public SecurityUser(User user) {
        super(user.getEmail(), user.getPassword(), Set.of(new SimpleGrantedAuthority(user.getRole().name())));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
