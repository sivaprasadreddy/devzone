package com.sivalabs.devzone.common;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.devzone.config.WebSecurityConfig;
import com.sivalabs.devzone.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(PROFILE_TEST)
@Import({WebSecurityConfig.class})
public abstract class AbstractWebMvcTest {
    @Autowired protected MockMvc mockMvc;

    @MockitoBean protected UserDetailsService userDetailsService;

    @MockitoBean protected SecurityService securityService;

    @MockitoBean protected PasswordEncoder passwordEncoder;

    // @MockitoBean protected RoleHierarchyImpl roleHierarchy;

    @Autowired protected ObjectMapper objectMapper;
}
