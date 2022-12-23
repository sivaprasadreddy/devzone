package com.sivalabs.devzone.users.usecases.viewprofile;

import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.users.domain.User;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AnyAuthenticatedUser
public class UserProfileController {
    private final ViewUserProfileHandler viewUserProfileHandler;

    public UserProfileController(ViewUserProfileHandler viewUserProfileHandler) {
        this.viewUserProfileHandler = viewUserProfileHandler;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, @CurrentUser User loginUser) {
        Optional<User> userOptional = viewUserProfileHandler.getUserById(loginUser.id());
        model.addAttribute("user", userOptional.orElseThrow());
        return "profile";
    }
}
