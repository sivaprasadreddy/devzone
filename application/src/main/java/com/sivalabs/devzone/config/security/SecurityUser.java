package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.users.domain.models.User;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final String name;

    public SecurityUser(User user) {
        super(
                user.email(),
                user.password(),
                Set.of(new SimpleGrantedAuthority(user.role().name())));

        this.name = user.name();
    }

    public String getName() {
        return name;
    }
}
