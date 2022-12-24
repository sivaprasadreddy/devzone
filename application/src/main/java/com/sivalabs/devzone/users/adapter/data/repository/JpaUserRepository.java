package com.sivalabs.devzone.users.adapter.data.repository;

import com.sivalabs.devzone.users.adapter.data.entity.UserEntity;
import com.sivalabs.devzone.users.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            """
    select new com.sivalabs.devzone.users.domain.model.User(u.id, u.name, u.email, u.password, u.role)
    from UserEntity u
    where u.email=:email
    """)
    Optional<User> findByEmail(String email);

    @Query(
            """
    select new com.sivalabs.devzone.users.domain.model.User(u.id, u.name, u.email, u.password, u.role)
    from UserEntity u
    where u.id=:id
    """)
    Optional<User> findByUserId(Long id);

    boolean existsByEmail(String email);

    @Modifying
    @Query("update UserEntity u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);
}
