package com.sivalabs.devzone.posts.adapter.data.repository;

import com.sivalabs.devzone.posts.adapter.data.mapper.CategoryMapper;
import com.sivalabs.devzone.posts.application.data.repository.CategoryRepository;
import com.sivalabs.devzone.posts.domain.model.Category;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
class CategoryRepositoryImpl implements CategoryRepository {
    private final JpaCategoryRepository jpaCategoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryRepositoryImpl(JpaCategoryRepository jpaCategoryRepository, CategoryMapper categoryMapper) {
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.categoryMapper = categoryMapper;
    }

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
