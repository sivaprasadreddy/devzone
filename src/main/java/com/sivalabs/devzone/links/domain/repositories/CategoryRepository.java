package com.sivalabs.devzone.links.domain.repositories;

import com.sivalabs.devzone.links.domain.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);

    List<Category> findAll();

    Category save(Category category);
}
