package com.sivalabs.devzone.posts.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.services.PostService;
import com.sivalabs.devzone.posts.web.mappers.PostDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;

@WebMvcTest(controllers = GetPostsController.class)
public class GetPostsControllerTest extends AbstractWebMvcTest {

    @MockBean protected PostService postService;
    @SpyBean protected PostDtoMapper postDTOMapper;

    @Test
    void shouldFetchPostsFirstPage() throws Exception {
        PagedResult<Post> postsDTO = new PagedResult<>(Page.empty());
        given(postService.getPosts(any(Integer.class))).willReturn(postsDTO);

        this.mockMvc
                .perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postsData"));
    }

    @Test
    void shouldFetchPostsByCategory() throws Exception {
        PagedResult<Post> postsDTO = new PagedResult<>(Page.empty());
        given(postService.getPostsByCategory(anyString(), any(Integer.class))).willReturn(postsDTO);

        this.mockMvc
                .perform(get("/posts?category=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postsData", "header"));
    }

    @Test
    void shouldFetchPostsBySearchKey() throws Exception {
        PagedResult<Post> postsDTO = new PagedResult<>(Page.empty());
        given(postService.searchPosts(anyString(), any(Integer.class))).willReturn(postsDTO);

        this.mockMvc
                .perform(get("/posts?query=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postsData", "header"));
    }
}
