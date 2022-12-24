package com.sivalabs.devzone.posts.usecases.createpost;

import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.posts.domain.models.CreatePostRequest;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.users.domain.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreatePostController {
    private static final Logger logger = LoggerFactory.getLogger(CreatePostController.class);
    private static final String MODEL_ATTRIBUTE_POST = "post";

    private final CreatePostHandler createPostHandler;

    public CreatePostController(CreatePostHandler createPostHandler) {
        this.createPostHandler = createPostHandler;
    }

    @GetMapping("/posts/new")
    @AnyAuthenticatedUser
    public String newPostForm(Model model) {
        model.addAttribute(MODEL_ATTRIBUTE_POST, new CreatePostRequest("", "", "", null));
        return "add-post";
    }

    @PostMapping("/posts")
    @AnyAuthenticatedUser
    public String createPost(
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_POST) CreatePostRequest request,
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
        if (bindingResult.hasErrors()) {
            return "add-post";
        }
        var createPostRequest =
                new CreatePostRequest(
                        request.url(), request.title(), request.category(), loginUser.id());
        Post post = createPostHandler.createPost(createPostRequest);
        logger.info("Post saved successfully with id: {}", post.id());
        return "redirect:/posts";
    }
}
