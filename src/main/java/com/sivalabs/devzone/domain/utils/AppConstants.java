package com.sivalabs.devzone.domain.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {
    public static final Long SYSTEM_USER_ID = 1L;

    public static final String PROFILE_PROD = "prod";
    public static final String PROFILE_NOT_PROD = "!" + PROFILE_PROD;
    public static final String PROFILE_HEROKU = "heroku";
    public static final String PROFILE_NOT_HEROKU = "!" + PROFILE_HEROKU;
}
