package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.services.PostService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final PostService postService;

    public CategoryRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Category> allCategories() {
        return postService.findAllCategories();
    }
}
