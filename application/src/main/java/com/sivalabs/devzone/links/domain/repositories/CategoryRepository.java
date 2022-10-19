package com.sivalabs.devzone.links.domain.repositories;

import com.sivalabs.devzone.links.domain.models.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();

    void upsert(Category category);
}
