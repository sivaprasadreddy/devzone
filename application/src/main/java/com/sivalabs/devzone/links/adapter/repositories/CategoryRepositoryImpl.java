package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.mappers.CategoryMapper;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.repositories.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CategoryRepositoryImpl implements CategoryRepository {
    private final JpaCategoryRepository jpaCategoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        Sort sort = Sort.by("name");
        return jpaCategoryRepository.findAllCategories(sort);
    }

    @Override
    public void upsert(Category category) {
        var entity = categoryMapper.toEntity(category);
        jpaCategoryRepository.upsert(entity);
    }
}
