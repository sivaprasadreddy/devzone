package com.sivalabs.devzone.posts.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.common.model.PagedResult;
import com.sivalabs.devzone.posts.application.usecases.getposts.GetPostsHandler;
import com.sivalabs.devzone.posts.application.usecases.getposts.PostDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

@WebMvcTest(controllers = GetPostsController.class)
public class GetPostsControllerTest extends AbstractWebMvcTest {

    @MockBean protected GetPostsHandler getPostsHandler;
    // @SpyBean protected PostDtoMapper postDTOMapper;

    @Test
    void shouldFetchPostsFirstPage() throws Exception {
        PagedResult<PostDTO> postsDTO = new PagedResult<>(Page.empty());
        given(getPostsHandler.getPosts(any(Integer.class))).willReturn(postsDTO);

        this.mockMvc
                .perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postsData"));
    }

    @Test
    void shouldFetchPostsByCategory() throws Exception {
        PagedResult<PostDTO> postsDTO = new PagedResult<>(Page.empty());
        given(getPostsHandler.getPostsByCategory(anyString(), any(Integer.class)))
                .willReturn(postsDTO);

        this.mockMvc
                .perform(get("/posts?category=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postsData", "header"));
    }

    @Test
    void shouldFetchPostsBySearchKey() throws Exception {
        PagedResult<PostDTO> postsDTO = new PagedResult<>(Page.empty());
        given(getPostsHandler.searchPosts(anyString(), any(Integer.class))).willReturn(postsDTO);

        this.mockMvc
                .perform(get("/posts?query=java"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postsData", "header"));
    }
}
