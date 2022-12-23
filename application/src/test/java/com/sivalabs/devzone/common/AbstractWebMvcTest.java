package com.sivalabs.devzone.common;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.devzone.config.security.SecurityService;
import com.sivalabs.devzone.config.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(PROFILE_TEST)
@Import({WebSecurityConfig.class})
public abstract class AbstractWebMvcTest {
    @Autowired protected MockMvc mockMvc;

    @MockBean protected UserDetailsService userDetailsService;

    @MockBean protected SecurityService securityService;

    @MockBean protected PasswordEncoder passwordEncoder;

    // @MockBean protected RoleHierarchyImpl roleHierarchy;

    @Autowired protected ObjectMapper objectMapper;
}
