package com.sivalabs.devzone.links.domain.repositories;

import com.sivalabs.devzone.links.domain.models.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;

public interface CategoryRepository {
    Optional<Category> findByName(String name);

    List<Category> findAll(Sort sort);

    Category save(Category category);
}
