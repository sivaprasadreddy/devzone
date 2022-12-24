package com.sivalabs.devzone.users.adapter.data;

import com.sivalabs.devzone.users.domain.User;
import com.sivalabs.devzone.users.gateways.data.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
