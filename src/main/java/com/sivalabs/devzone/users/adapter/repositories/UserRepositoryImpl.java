package com.sivalabs.devzone.users.adapter.repositories;

import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import com.sivalabs.devzone.users.adapter.mappers.UserMapper;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return jpaUserRepository.findByUserId(id);
    }

    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedUser = jpaUserRepository.save(userEntity);
        return userMapper.toModel(savedUser);
    }

    public void delete(User user) {
        jpaUserRepository.deleteById(user.getId());
    }
}
