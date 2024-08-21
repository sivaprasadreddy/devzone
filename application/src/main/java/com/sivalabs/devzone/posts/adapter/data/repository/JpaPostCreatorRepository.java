package com.sivalabs.devzone.posts.adapter.data.repository;

import com.sivalabs.devzone.users.adapter.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaPostCreatorRepository extends JpaRepository<UserEntity, Long> {}
