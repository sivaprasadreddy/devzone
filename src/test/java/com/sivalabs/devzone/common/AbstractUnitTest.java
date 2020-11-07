package com.sivalabs.devzone.common;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_TEST;

@ActiveProfiles(PROFILE_TEST)
@Import(TestConfig.class)
public abstract class AbstractUnitTest {
}
