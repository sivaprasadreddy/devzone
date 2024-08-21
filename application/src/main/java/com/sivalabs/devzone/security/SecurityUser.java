package com.sivalabs.devzone.security;

import com.sivalabs.devzone.users.domain.model.User;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        SecurityUser that = (SecurityUser) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }
}
