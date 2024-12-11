package com.sivalabs.devzone.users.web.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.security.SecurityUser;
import com.sivalabs.devzone.users.application.usecases.viewprofile.ViewUserProfileHandler;
import com.sivalabs.devzone.users.domain.model.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = UserProfileController.class)
class UserProfileControllerUnitTests extends AbstractWebMvcTest {
    @MockitoBean
    private ViewUserProfileHandler viewUserProfileHandler;

    @Test
    void shouldViewProfile() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        given(viewUserProfileHandler.getUserById(anyLong())).willReturn(Optional.of(user));

        mockMvc.perform(get("/profile").with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRedirectToLoginPageWhenUnauthorized() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", Matchers.containsString("/login")));
    }
}
