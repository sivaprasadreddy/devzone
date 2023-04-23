package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.posts.application.data.repository.CategoryRepository;
import com.sivalabs.devzone.posts.domain.model.Category;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class GetCategoriesController {
    private final CategoryRepository categoryRepository;

    public GetCategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }
}
