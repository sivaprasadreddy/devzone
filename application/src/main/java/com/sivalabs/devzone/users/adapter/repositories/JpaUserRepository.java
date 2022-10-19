package com.sivalabs.devzone.users.adapter.repositories;

import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import com.sivalabs.devzone.users.domain.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            """
    select new com.sivalabs.devzone.users.domain.models.User(u.id, u.name, u.email, u.password, u.role)
    from UserEntity u
    where u.email=:email
    """)
    Optional<User> findByEmail(String email);

    @Query(
            """
    select new com.sivalabs.devzone.users.domain.models.User(u.id, u.name, u.email, u.password, u.role)
    from UserEntity u
    where u.id=:id
    """)
    Optional<User> findByUserId(Long id);

    boolean existsByEmail(String email);
}
