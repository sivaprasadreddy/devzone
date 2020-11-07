package com.sivalabs.devzone.web.api.resources;

import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.services.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Slf4j
public class TagRestController {
    private final LinkService linkService;

    @GetMapping
    public List<Tag> allTags() {
        return linkService.findAllTags();
    }

}
