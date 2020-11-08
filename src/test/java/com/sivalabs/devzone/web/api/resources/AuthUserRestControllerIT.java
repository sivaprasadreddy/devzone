package com.sivalabs.devzone.web.api.resources;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthUserRestControllerIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser("siva@gmail.com")
    void should_get_login_user_details() throws Exception {
        this.mockMvc.perform(get("/api/user")).andExpect(status().isOk());
    }

    @Test
    void should_fail_to_get_login_user_details_if_unauthorized() throws Exception {
        this.mockMvc.perform(get("/api/user")).andExpect(status().isForbidden());
    }
}
