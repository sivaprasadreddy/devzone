package com.sivalabs.devzone.utils;

import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.users.domain.RoleEnum;
import com.sivalabs.devzone.users.domain.User;

public class TestDataFactory {

    public static Post getMockPost(Long postId, Long userId) {
        Category category = new Category(1L, "Java");
        return new Post(
                postId,
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
