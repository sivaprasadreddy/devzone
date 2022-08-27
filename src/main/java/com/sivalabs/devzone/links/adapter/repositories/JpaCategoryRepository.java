package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.entities.CategoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
