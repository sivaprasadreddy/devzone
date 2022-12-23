package com.sivalabs.devzone.users.web.controllers;

import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.services.UserService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AnyAuthenticatedUser
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, @CurrentUser User loginUser) {
        Optional<User> userOptional = userService.getUserById(loginUser.getId());
        model.addAttribute("user", userOptional.orElseThrow());
        return "profile";
    }
}
