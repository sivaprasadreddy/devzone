package com.sivalabs.devzone.users.usecases.changepassword;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.users.domain.User;
import com.sivalabs.devzone.utils.TestDataFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = ChangePasswordController.class)
public class ChangePasswordControllerTest extends AbstractWebMvcTest {
    @MockBean private ChangePasswordHandler changePasswordHandler;

    @Test
    void shouldShowChangePasswordFormPage() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);

        mockMvc.perform(get("/change-password").with(user(securityUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("change-password"))
                .andExpect(model().attributeExists("changePassword"));
    }

    @Test
    void shouldChangePasswordSuccessfully() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        willDoNothing()
                .given(changePasswordHandler)
                .changePassword(anyString(), any(ChangePasswordRequest.class));

        mockMvc.perform(
                        post("/change-password")
                                .with(csrf())
                                .with(user(securityUser))
                                .param("oldPassword", "siva")
                                .param("newPassword", "siva123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("msg", "Password changed successfully"))
                .andExpect(header().string("Location", "/change-password"));
    }

    @Test
    void shouldRedisplayChangePasswordFormPageWhenSubmittedInvalidData() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        mockMvc.perform(
                        post("/change-password")
                                .with(csrf())
                                .with(user(securityUser))
                                .param("oldPassword", "")
                                .param("newPassword", ""))
                .andExpect(model().hasErrors())
                .andExpect(
                        model().attributeHasFieldErrors(
                                        "changePassword", "oldPassword", "newPassword"))
                .andExpect(
                        model().attributeHasFieldErrorCode(
                                        "changePassword", "oldPassword", "NotBlank"))
                .andExpect(
                        model().attributeHasFieldErrorCode(
                                        "changePassword", "newPassword", "NotBlank"))
                .andExpect(view().name("change-password"));
    }

    @Test
    void shouldRedisplayChangePasswordFormPageWhenCurrentPasswordIsInvalid() throws Exception {
        User user = TestDataFactory.getMockUser();
        SecurityUser securityUser = new SecurityUser(user);
        given(securityService.loginUser()).willReturn(user);
        willThrow(new DevZoneException("Current password is not matching"))
                .given(changePasswordHandler)
                .changePassword(anyString(), any(ChangePasswordRequest.class));

        mockMvc.perform(
                        post("/change-password")
                                .with(csrf())
                                .with(user(securityUser))
                                .param("oldPassword", "siva")
                                .param("newPassword", "siva123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("msg", "Current password is not matching"))
                .andExpect(header().string("Location", "/change-password"));
    }

    @Test
    void shouldRedirectToLoginPageWhenUnauthorized() throws Exception {
        mockMvc.perform(post("/change-password").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", Matchers.containsString("/login")));
    }
}
