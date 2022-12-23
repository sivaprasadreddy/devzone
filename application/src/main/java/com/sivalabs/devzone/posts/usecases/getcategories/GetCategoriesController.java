package com.sivalabs.devzone.posts.usecases.getcategories;

import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.gateways.data.repository.CategoryRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
