package com.sivalabs.devzone.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.devzone.config.security.SecurityUtils;
import com.sivalabs.devzone.config.security.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_TEST;

@ActiveProfiles(PROFILE_TEST)
public abstract class AbstractWebMvcTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserDetailsService userDetailsService;

    @MockBean
    protected TokenHelper tokenHelper;

    @MockBean
    protected SecurityProblemSupport problemSupport;

    @MockBean
    protected SecurityUtils securityUtils;

    @MockBean
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;
}
