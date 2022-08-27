package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaLinkCreatorRepository extends JpaRepository<UserEntity, Long> {}
