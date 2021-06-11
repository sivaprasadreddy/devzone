package com.sivalabs.devzone.web.controllers;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.annotations.CurrentUser;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import com.sivalabs.devzone.domain.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.UserService;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new CreateUserRequest());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @Valid @ModelAttribute("user") CreateUserRequest createUserRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userService.createUser(createUserRequest);
            redirectAttributes.addFlashAttribute("msg", "Registration is successful");
        } catch (ResourceAlreadyExistsException e) {
            log.error("Registration err", e);
            bindingResult.rejectValue("email", "email.exists", e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    @AnyAuthenticatedUser
    public String viewProfile(Model model, @CurrentUser User loginUser) {
        Optional<UserDTO> userOptional = userService.getUserById(loginUser.getId());
        model.addAttribute("user", userOptional.get());
        return "profile";
    }

    @GetMapping("/change-password")
    @AnyAuthenticatedUser
    public String changePasswordForm(Model model) {
        model.addAttribute("changePassword", new ChangePasswordRequest());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePassword") ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            @CurrentUser User loginUser,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "change-password";
        }
        try {
            userService.changePassword(loginUser.getEmail(), changePasswordRequest);
            redirectAttributes.addFlashAttribute("msg", "Password changed successfully");
        } catch (DevZoneException e) {
            log.error("ChangePassword err", e);
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }
        return "redirect:/change-password";
    }
}
