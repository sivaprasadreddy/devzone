package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.services.LinkService;
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
    private final LinkService linkService;

    @GetMapping
    // @PreAuthorize("permitAll()")
    public List<Category> allCategories() {
        return linkService.findAllCategories();
    }
}
