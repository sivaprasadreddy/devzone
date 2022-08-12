package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
