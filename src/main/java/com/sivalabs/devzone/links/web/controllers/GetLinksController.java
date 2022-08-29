package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.links.domain.models.LinksDTO;
import com.sivalabs.devzone.links.domain.services.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GetLinksController {
    private static final String PAGINATION_PREFIX = "paginationPrefix";

    private final LinkService linkService;

    @GetMapping("/links")
    public String home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model) {
        LinksDTO data;
        if (StringUtils.isNotEmpty(category)) {
            log.info("Fetching links for category {} with page: {}", category, page);
            data = linkService.getLinksByCategory(category, page);
            model.addAttribute("header", "Links by Category : " + category);
            model.addAttribute(PAGINATION_PREFIX, "/links?category=" + category);
        } else if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, page);
            data = linkService.searchLinks(query, page);
            model.addAttribute("header", "Search Results for : " + query);
            model.addAttribute(PAGINATION_PREFIX, "/links?query=" + query);
        } else {
            log.info("Fetching links with page: {}", page);
            data = linkService.getAllLinks(page);
            model.addAttribute(PAGINATION_PREFIX, "/links?");
        }
        model.addAttribute("linksData", data);
        model.addAttribute("categories", linkService.findAllCategories());
        return "links";
    }
}
