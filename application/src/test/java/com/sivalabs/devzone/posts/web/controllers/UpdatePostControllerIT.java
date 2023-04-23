package com.sivalabs.devzone.posts.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.posts.application.data.repository.CategoryRepository;
import com.sivalabs.devzone.posts.application.data.repository.PostRepository;
import com.sivalabs.devzone.posts.domain.model.Category;
import com.sivalabs.devzone.posts.domain.model.Post;
import com.sivalabs.devzone.users.application.data.repository.UserRepository;
import com.sivalabs.devzone.users.domain.model.User;
import com.sivalabs.devzone.utils.TestDataFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

public class UpdatePostControllerIT extends AbstractIntegrationTest {

    @Autowired PostRepository postRepository;

    @Autowired UserRepository userRepository;

    @Autowired CategoryRepository categoryRepository;

    Post post = null;

    @BeforeEach
    void setUp() {
        User user = userRepository.findByEmail("user@gmail.com").orElseThrow();
        post = TestDataFactory.getMockPost(null, user.id());
        post = postRepository.save(post);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void shouldShowEditPostFormPage() throws Exception {
        mockMvc.perform(get("/posts/{id}/edit", post.id()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void shouldUpdatePostSuccessfully() throws Exception {
        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void shouldNotBeAbleToUpdateOthersPost() throws Exception {
        User admin = userRepository.findByEmail("admin@gmail.com").orElseThrow();
        Category category = categoryRepository.findAll().get(0);
        Post post = new Post(null, "https://google.com", "Google", category, admin, null, null);
        post = postRepository.save(post);
        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void adminShouldBeAbleToUpdateOthersPost() throws Exception {
        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));
    }
}
