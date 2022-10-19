package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.users.domain.models.User;
import java.util.Set;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@EqualsAndHashCode(callSuper = true)
public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final String name;

    public SecurityUser(User user) {
        super(
                user.getEmail(),
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority(user.getRole().name())));

        this.name = user.getName();
    }

    public String getName() {
        return name;
    }
}
