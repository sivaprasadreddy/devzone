package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.users.domain.models.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@Slf4j
public class RoleHierarchyConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(RoleEnum.getRoleHierarchy());
        log.debug("RoleHierarchy: {}", RoleEnum.getRoleHierarchy());
        return roleHierarchy;
    }
}
