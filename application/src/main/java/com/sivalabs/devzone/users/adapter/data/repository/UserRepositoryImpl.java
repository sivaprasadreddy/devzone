package com.sivalabs.devzone.users.adapter.data.repository;

import com.sivalabs.devzone.users.adapter.data.entity.UserEntity;
import com.sivalabs.devzone.users.adapter.data.mapper.UserMapper;
import com.sivalabs.devzone.users.application.data.repository.UserRepository;
import com.sivalabs.devzone.users.domain.model.User;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findByUserId(id);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedUser = jpaUserRepository.save(userEntity);
        return userMapper.toModel(savedUser);
    }

    @Override
    public void delete(User user) {
        jpaUserRepository.deleteById(user.id());
    }

    @Override
    public void updatePassword(String email, String password) {
        jpaUserRepository.updatePassword(email, password);
    }
}
