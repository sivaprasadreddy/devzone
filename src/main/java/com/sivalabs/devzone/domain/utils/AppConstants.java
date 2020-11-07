package com.sivalabs.devzone.domain.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {
    public static final Long SYSTEM_USER_ID = 1L;

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";

    public static final String PROFILE_PROD = "prod";
    public static final String PROFILE_NOT_PROD = "!" + PROFILE_PROD;
    public static final String PROFILE_HEROKU = "heroku";
    public static final String PROFILE_NOT_HEROKU = "!" + PROFILE_HEROKU;
    public static final String PROFILE_TEST = "test";
    public static final String PROFILE_IT = "integration-test";
}
