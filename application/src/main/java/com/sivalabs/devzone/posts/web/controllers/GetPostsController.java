package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.common.model.PagedResult;
import com.sivalabs.devzone.config.logging.Loggable;
import com.sivalabs.devzone.posts.application.usecases.getposts.GetPostsHandler;
import com.sivalabs.devzone.posts.application.usecases.getposts.PostDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Loggable
public class GetPostsController {
    private static final Logger logger = LoggerFactory.getLogger(GetPostsController.class);
    private static final String PAGINATION_PREFIX = "paginationPrefix";

    private final GetPostsHandler getPostsHandler;

    public GetPostsController(GetPostsHandler getPostsHandler) {
        this.getPostsHandler = getPostsHandler;
    }

    @GetMapping("/posts")
    public String home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model) {
        PagedResult<PostDTO> data;
        if (StringUtils.isNotEmpty(category)) {
            logger.info("Fetching posts for category {} with page: {}", category, page);
            data = getPostsHandler.getPostsByCategory(category, page);
            model.addAttribute("header", "Posts by category : " + category);
            model.addAttribute(PAGINATION_PREFIX, "/posts?category=" + category);
        } else if (StringUtils.isNotEmpty(query)) {
            logger.info("Searching posts for {} with page: {}", query, page);
            data = getPostsHandler.searchPosts(query, page);
            model.addAttribute("header", "Search Results for : " + query);
            model.addAttribute(PAGINATION_PREFIX, "/posts?query=" + query);
        } else {
            logger.info("Fetching posts with page: {}", page);
            data = getPostsHandler.getPosts(page);
            model.addAttribute(PAGINATION_PREFIX, "/posts?");
        }
        model.addAttribute("postsData", data);
        model.addAttribute("categories", getPostsHandler.getAllCategories());
        return "posts";
    }
}
