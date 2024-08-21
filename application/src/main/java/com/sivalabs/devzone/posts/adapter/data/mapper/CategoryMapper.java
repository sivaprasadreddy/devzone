package com.sivalabs.devzone.posts.adapter.data.mapper;

import com.sivalabs.devzone.posts.adapter.data.entity.CategoryEntity;
import com.sivalabs.devzone.posts.domain.model.Category;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toModel(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        return new Category(
                categoryEntity.getId(),
                categoryEntity.getName(),
                Set.of(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getUpdatedAt());
    }

    public CategoryEntity toEntity(Category category) {
        if (category == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.id());
        categoryEntity.setName(category.name());
        categoryEntity.setCreatedAt(category.createdAt());
        categoryEntity.setUpdatedAt(category.updatedAt());
        return categoryEntity;
    }
}
