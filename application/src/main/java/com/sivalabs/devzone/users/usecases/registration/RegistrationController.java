package com.sivalabs.devzone.users.usecases.registration;

import com.sivalabs.devzone.common.exceptions.ResourceAlreadyExistsException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private static final String registrationView = "registration";

    private final CreateUserHandler createUserHandler;

    public RegistrationController(CreateUserHandler createUserHandler) {
        this.createUserHandler = createUserHandler;
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new CreateUserRequest("", "", ""));
        return registrationView;
    }

    @PostMapping("/registration")
    public String registerUser(
            @Valid @ModelAttribute("user") CreateUserRequest createUserRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return registrationView;
        }
        try {
            createUserHandler.createUser(createUserRequest);
            redirectAttributes.addFlashAttribute("msg", "Registration is successful");
        } catch (ResourceAlreadyExistsException e) {
            log.error("Registration err", e);
            bindingResult.rejectValue("email", "email.exists", e.getMessage());
            return registrationView;
        }
        return "redirect:/login";
    }
}
