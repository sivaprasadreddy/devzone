package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.models.UpdatePostRequest;
import com.sivalabs.devzone.posts.domain.services.PostService;
import com.sivalabs.devzone.users.domain.models.User;
import jakarta.validation.Valid;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UpdatePostController {
    private static final String MODEL_ATTRIBUTE_POST = "post";
    private static final Logger log = LoggerFactory.getLogger(UpdatePostController.class);

    private final PostService postService;

    public UpdatePostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{id}/edit")
    @AnyAuthenticatedUser
    public String editPostForm(@PathVariable Long id, @CurrentUser User loginUser, Model model) {
        Post post = postService.getPostById(id).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        this.checkPrivilege(post, loginUser);
        String category = null;
        if (post.category() != null) {
            category = post.category().name();
        }
        UpdatePostRequest updatePostRequest =
                new UpdatePostRequest(id, post.url(), post.title(), category);

        model.addAttribute(MODEL_ATTRIBUTE_POST, updatePostRequest);
        return "edit-post";
    }

    @PutMapping("/posts/{id}")
    @AnyAuthenticatedUser
    public String updateBookmark(
            @PathVariable Long id,
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_POST) UpdatePostRequest request,
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
        if (bindingResult.hasErrors()) {
            return "edit-post";
        }
        Post post = postService.getPostById(id).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        var updatePostRequest =
                new UpdatePostRequest(id, request.url(), request.title(), request.category());
        this.checkPrivilege(post, loginUser);
        Post updatedPost = postService.updatePost(updatePostRequest);
        log.info("Post with id: {} updated successfully", updatedPost.id());
        return "redirect:/posts";
    }

    private void checkPrivilege(Post post, User loginUser) {
        if (!(Objects.equals(post.createdBy().id(), loginUser.id())
                || loginUser.isAdminOrModerator())) {
            throw new UnauthorisedAccessException("Permission Denied");
        }
    }
}
