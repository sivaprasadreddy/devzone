package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.config.logging.Loggable;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.models.PostDTO;
import com.sivalabs.devzone.posts.domain.services.PostService;
import com.sivalabs.devzone.posts.web.mappers.PostDtoMapper;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Loggable
public class GetPostsController {
    private static final Logger log = LoggerFactory.getLogger(GetPostsController.class);
    private static final String PAGINATION_PREFIX = "paginationPrefix";

    private final PostService postService;
    private final PostDtoMapper postDTOMapper;

    public GetPostsController(PostService postService, PostDtoMapper postDTOMapper) {
        this.postService = postService;
        this.postDTOMapper = postDTOMapper;
    }

    @GetMapping("/posts")
    public String home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model) {
        PagedResult<PostDTO> data;
        if (StringUtils.isNotEmpty(category)) {
            log.info("Fetching posts for category {} with page: {}", category, page);
            data = convert(postService.getPostsByCategory(category, page));
            model.addAttribute("header", "Posts by category : " + category);
            model.addAttribute(PAGINATION_PREFIX, "/posts?category=" + category);
        } else if (StringUtils.isNotEmpty(query)) {
            log.info("Searching posts for {} with page: {}", query, page);
            data = convert(postService.searchPosts(query, page));
            model.addAttribute("header", "Search Results for : " + query);
            model.addAttribute(PAGINATION_PREFIX, "/posts?query=" + query);
        } else {
            log.info("Fetching posts with page: {}", page);
            data = convert(postService.getPosts(page));
            model.addAttribute(PAGINATION_PREFIX, "/posts?");
        }
        model.addAttribute("postsData", data);
        model.addAttribute("categories", postService.findAllCategories());
        return "posts";
    }

    private PagedResult<PostDTO> convert(PagedResult<Post> postsPage) {
        List<PostDTO> postDTOs = postDTOMapper.toDTOs(postsPage.getData());
        Page<PostDTO> postDTOsPage =
                new PageImpl<>(postDTOs, postsPage.getPageable(), postsPage.getTotalElements());
        return new PagedResult<>(postDTOsPage);
    }
}
