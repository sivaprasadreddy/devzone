package com.sivalabs.devzone.utils;

import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.users.domain.models.RoleEnum;
import com.sivalabs.devzone.users.domain.models.User;

public class TestDataFactory {

    public static Link getMockLink(Long linkId, Long userId) {
        Link link = new Link();
        link.setId(linkId);
        link.setUrl("https://google.com");
        link.setTitle("Google");
        Category category = new Category(1L, "Java");
        link.setCategory(category);
        link.setCreatedBy(getMockUser(userId, RoleEnum.ROLE_USER));
        return link;
    }

    public static User getMockUser() {
        return getMockUser(1L, RoleEnum.ROLE_USER);
    }

    public static User getMockUser(Long id, RoleEnum role) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@gmail.com");
        user.setPassword("user");
        user.setRole(role);
        return user;
    }
}
