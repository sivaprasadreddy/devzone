package com.sivalabs.devzone.posts.application.data.repository;

import com.sivalabs.devzone.posts.domain.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();

    void upsert(Category category);
}
