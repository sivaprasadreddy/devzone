package com.sivalabs.devzone.utils;

import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;

public class TestDataFactory {

    public static Link getMockLink(Long linkId, Long userId) {
        Category category = new Category(1L, "Java");
        return new Link(
                linkId,
                "https://google.com",
                "Google",
                category,
                getMockUser(userId, RoleEnum.ROLE_USER),
                null,
                null);
    }

    public static User getMockUser() {
        return getMockUser(1L, RoleEnum.ROLE_USER);
    }

    public static User getMockUser(Long id, RoleEnum role) {
        return new User(id, "user", "user@gmail.com", "user", role);
    }
}
