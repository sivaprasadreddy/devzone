package com.sivalabs.devzone.links.adapter.mappers;

import com.sivalabs.devzone.links.adapter.entities.CategoryEntity;
import com.sivalabs.devzone.links.domain.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toModel(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setId(category.getId());
        category.setName(categoryEntity.getName());
        category.setCreatedAt(categoryEntity.getCreatedAt());
        category.setUpdatedAt(categoryEntity.getUpdatedAt());
        return category;
    }

    public CategoryEntity toEntity(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        categoryEntity.setCreatedAt(category.getCreatedAt());
        categoryEntity.setUpdatedAt(category.getUpdatedAt());
        return categoryEntity;
    }
}
