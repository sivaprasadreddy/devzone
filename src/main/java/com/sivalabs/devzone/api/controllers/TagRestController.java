package com.sivalabs.devzone.api.controllers;

import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.services.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Slf4j
public class TagRestController {
    private final TagService tagService;

    @GetMapping
    public List<Tag> allTags() {
        return tagService.findAllTags();
    }
}
