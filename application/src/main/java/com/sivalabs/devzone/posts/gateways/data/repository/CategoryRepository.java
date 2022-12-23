package com.sivalabs.devzone.posts.gateways.data.repository;

import com.sivalabs.devzone.posts.domain.models.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();

    void upsert(Category category);
}
