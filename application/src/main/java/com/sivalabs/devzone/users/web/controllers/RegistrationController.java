package com.sivalabs.devzone.users.web.controllers;

import com.sivalabs.devzone.common.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.users.domain.models.CreateUserRequest;
import com.sivalabs.devzone.users.domain.services.UserService;
import jakarta.validation.Valid;
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
public class RegistrationController {
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
}
