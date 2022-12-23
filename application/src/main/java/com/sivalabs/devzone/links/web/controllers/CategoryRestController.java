package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.services.LinkService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final LinkService linkService;

    public CategoryRestController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public List<Category> allCategories() {
        return linkService.findAllCategories();
    }
}
