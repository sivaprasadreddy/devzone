package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.services.PostService;
import com.sivalabs.devzone.users.domain.models.User;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class DeletePostController {
    private final PostService postService;

    public DeletePostController(PostService postService) {
        this.postService = postService;
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus
    @AnyAuthenticatedUser
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @CurrentUser User loginUser) {
        Post post = postService.getPostById(id).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        this.checkPrivilege(post, loginUser);
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(Post post, User loginUser) {
        if (!(Objects.equals(post.createdBy().id(), loginUser.id())
                || loginUser.isAdminOrModerator())) {
            throw new UnauthorisedAccessException("Permission Denied");
        }
    }
}
