package com.sivalabs.devzone.posts.usecases.getposts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GetPostsControllerIT extends AbstractIntegrationTest {

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldShowPostsByPage(int pageNo) throws Exception {
        mockMvc.perform(get("/posts?page={page}", pageNo))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("paginationPrefix"))
                .andExpect(model().attributeExists("postsData"))
                .andExpect(model().attributeExists("categories"));
    }

    @ParameterizedTest
    @CsvSource({"spring", "gradle"})
    void shouldSearchPostsByQuery(String query) throws Exception {
        mockMvc.perform(get("/posts?query={query}", query))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("paginationPrefix"))
                .andExpect(model().attributeExists("header"))
                .andExpect(model().attributeExists("postsData"))
                .andExpect(model().attributeExists("categories"));
    }

    @ParameterizedTest
    @CsvSource({"spring-boot", "gradle"})
    void shouldFetchPostsByCategory(String category) throws Exception {
        mockMvc.perform(get("/posts?category={category}", category))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("paginationPrefix"))
                .andExpect(model().attributeExists("header"))
                .andExpect(model().attributeExists("postsData"))
                .andExpect(model().attributeExists("categories"));
    }
}
