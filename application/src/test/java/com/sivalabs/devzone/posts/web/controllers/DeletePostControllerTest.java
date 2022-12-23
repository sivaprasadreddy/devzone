package com.sivalabs.devzone.posts.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.services.PostService;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = DeletePostController.class)
public class DeletePostControllerTest extends AbstractWebMvcTest {
    @MockBean protected PostService postService;

    @Test
    void shouldBeAbleToDeleteOwnPostSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 1L);
        given(postService.getPostById(any(Long.class))).willReturn(Optional.of(post));

        mockMvc.perform(delete("/posts/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isOk());
    }

    @Test
    void adminShouldBeAbleToDeleteOthersPostSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_ADMIN);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 1L);
        given(postService.getPostById(any(Long.class))).willReturn(Optional.of(post));

        mockMvc.perform(delete("/posts/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isOk());
    }

    @Test
    void normalUserShouldNotBeAbleToDeleteOthersPost() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_USER);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 2L);
        given(postService.getPostById(any(Long.class))).willReturn(Optional.of(post));

        mockMvc.perform(delete("/posts/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void return404IfPostNotExists() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_USER);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(postService.getPostById(any(Long.class))).willReturn(Optional.empty());

        mockMvc.perform(delete("/posts/{id}", 1).with(csrf()).with(user(securityUser)))
                .andExpect(status().isNotFound());
    }
}
