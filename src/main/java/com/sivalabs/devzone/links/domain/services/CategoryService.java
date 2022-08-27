package com.sivalabs.devzone.links.domain.services;

import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.repositories.CategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Cacheable("categories")
    public List<Category> findAllCategories() {
        Sort sort = Sort.by("name");
        return categoryRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    @Cacheable("category-by-name")
    public Optional<Category> findCategoryByName(String category) {
        return categoryRepository.findByName(category);
    }

    @CacheEvict(
            cacheNames = {"categories", "category-by-name"},
            allEntries = true)
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
