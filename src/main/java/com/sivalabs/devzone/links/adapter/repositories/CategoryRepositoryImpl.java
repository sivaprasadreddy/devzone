package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.mappers.CategoryMapper;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.repositories.CategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class CategoryRepositoryImpl implements CategoryRepository {
    private final JpaCategoryRepository jpaCategoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Optional<Category> findByName(String name) {
        return jpaCategoryRepository.findByName(name).map(categoryMapper::toModel);
    }

    @Override
    public List<Category> findAll() {
        Sort sort = Sort.by("name");
        return jpaCategoryRepository.findAllCategories(sort);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Category save(Category category) {
        var entity = categoryMapper.toEntity(category);
        var savedCategory = jpaCategoryRepository.save(entity);
        return categoryMapper.toModel(savedCategory);
    }
}
