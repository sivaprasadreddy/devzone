package com.sivalabs.devzone.posts.usecases.updatepost;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.users.domain.RoleEnum;
import com.sivalabs.devzone.users.domain.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = UpdatePostController.class)
public class UpdatePostControllerTest extends AbstractWebMvcTest {

    @MockBean protected UpdatePostHandler updatePostHandler;

    @Test
    void shouldShowNotFoundWhenUpdatingPostNotExists() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(updatePostHandler.getPostById(any(Long.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/posts/{id}/edit", 1).with(user(securityUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldShowEditPostFormPage() throws Exception {
        Post post = TestDataFactory.getMockPost(1L, 1L);
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(updatePostHandler.getPostById(any(Long.class))).willReturn(Optional.of(post));

        mockMvc.perform(get("/posts/{id}/edit", 1).with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void shouldFailToUpdatePostIfUrlIsEmpty() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 1L);
        given(updatePostHandler.getPostById(1L)).willReturn(Optional.of(post));
        given(updatePostHandler.updatePost(any(UpdatePostRequest.class))).willReturn(post);

        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("post", "url"))
                .andExpect(view().name("edit-post"));
    }

    @Test
    void shouldUpdatePostSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 1L);
        given(updatePostHandler.getPostById(1L)).willReturn(Optional.of(post));
        given(updatePostHandler.updatePost(any(UpdatePostRequest.class))).willReturn(post);

        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));
    }

    @Test
    void shouldNotBeAbleToUpdateOthersPost() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_USER);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 2L);
        given(updatePostHandler.getPostById(post.id())).willReturn(Optional.of(post));
        given(updatePostHandler.updatePost(any(UpdatePostRequest.class))).willReturn(post);

        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is(403));
    }

    @Test
    void adminShouldBeAbleToUpdateOthersPost() throws Exception {
        User user = TestDataFactory.getMockUser(1L, RoleEnum.ROLE_ADMIN);
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        Post post = TestDataFactory.getMockPost(1L, 2L);
        given(updatePostHandler.getPostById(post.id())).willReturn(Optional.of(post));
        given(updatePostHandler.updatePost(any(UpdatePostRequest.class))).willReturn(post);

        mockMvc.perform(
                        put("/posts/{id}", post.id())
                                .with(csrf())
                                .with(user(securityUser))
                                .param("url", "https://sivalabs.in")
                                .param("title", "SivaLabs")
                                .param("category", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));
    }
}
