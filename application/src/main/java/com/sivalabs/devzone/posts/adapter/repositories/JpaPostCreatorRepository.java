package com.sivalabs.devzone.posts.adapter.repositories;

import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaPostCreatorRepository extends JpaRepository<UserEntity, Long> {}
