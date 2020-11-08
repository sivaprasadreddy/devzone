package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
