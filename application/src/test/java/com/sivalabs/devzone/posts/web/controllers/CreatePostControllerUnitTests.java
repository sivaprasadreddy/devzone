package com.sivalabs.devzone.posts.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.posts.application.usecases.createpost.CreatePostHandler;
import com.sivalabs.devzone.posts.application.usecases.createpost.CreatePostRequest;
import com.sivalabs.devzone.posts.domain.model.Post;
import com.sivalabs.devzone.security.SecurityUser;
import com.sivalabs.devzone.users.domain.model.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = CreatePostController.class)
public class CreatePostControllerUnitTests extends AbstractWebMvcTest {

    @MockitoBean
    protected CreatePostHandler createPostHandler;

    @Test
    void shouldShowCreatePostFormPage() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);

        mockMvc.perform(get("/posts/new").with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void shouldCreatePostSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = new Post(1L, "https://sivalabs.in", "SivaLabs", null, user, null, null);
        given(createPostHandler.createPost(any(CreatePostRequest.class))).willReturn(post);
        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .with(user(securityUser))
                        .param("url", "https://sivalabs.in")
                        .param("title", "SivaLabs")
                        .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));
    }

    @Test
    void shouldFailToCreatePostIfUrlIsEmpty() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = new Post(1L, "https://sivalabs.in", "SivaLabs", null, user, null, null);
        given(createPostHandler.createPost(any(CreatePostRequest.class))).willReturn(post);

        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .with(user(securityUser))
                        .param("url", "")
                        .param("title", "SivaLabs")
                        .param("category", "java"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("post", "url"))
                .andExpect(view().name("add-post"));
    }
}
