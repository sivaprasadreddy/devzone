package com.sivalabs.devzone.posts.usecases.getposts;

import com.sivalabs.devzone.config.security.SecurityService;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.users.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PostDtoMapper {
    private final SecurityService securityService;

    public PostDtoMapper(SecurityService securityService) {
        this.securityService = securityService;
    }

    public List<PostDTO> toDTOs(List<Post> posts) {
        if (posts == null) {
            return new ArrayList<>(0);
        }
        User loginUser = securityService.loginUser();
        return posts.stream().map(post -> this.toDTO(loginUser, post)).toList();
    }

    public PostDTO toDTO(User loginUser, Post post) {
        String category = null;
        if (post.category() != null) {
            category = post.category().name();
        }
        boolean editable = this.canCurrentUserEditPost(loginUser, post);
        return new PostDTO(
                post.id(),
                post.url(),
                post.title(),
                category,
                post.createdBy().id(),
                post.createdBy().name(),
                post.createdAt(),
                post.updatedAt(),
                editable);
    }

    public boolean canCurrentUserEditPost(User loginUser, Post post) {
        return loginUser != null
                && post != null
                && (Objects.equals(post.createdBy().id(), loginUser.id())
                        || loginUser.isAdminOrModerator());
    }
}
