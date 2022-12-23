package com.sivalabs.devzone.posts.domain.repositories;

import com.sivalabs.devzone.posts.domain.models.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();

    void upsert(Category category);
}
