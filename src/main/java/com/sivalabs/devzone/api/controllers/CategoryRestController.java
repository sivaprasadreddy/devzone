package com.sivalabs.devzone.api.controllers;

import com.sivalabs.devzone.domain.entities.Category;
import com.sivalabs.devzone.domain.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryRestController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> allCategories() {
        return categoryService.findAllCategories();
    }
}
