package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.links.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.users.domain.models.User;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CreateLinkController {
    private static final String MODEL_ATTRIBUTE_LINK = "link";

    private final LinkService linkService;

    @GetMapping("/links/new")
    @AnyAuthenticatedUser
    public String newLinkForm(Model model) {
        model.addAttribute(MODEL_ATTRIBUTE_LINK, new CreateLinkRequest());
        return "add-link";
    }

    @PostMapping("/links")
    @AnyAuthenticatedUser
    public String createLink(
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_LINK) CreateLinkRequest createLinkRequest,
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
        if (bindingResult.hasErrors()) {
            return "add-link";
        }
        createLinkRequest.setCreatedUserId(loginUser.getId());
        Link link = linkService.createLink(createLinkRequest);
        log.info("Link saved successfully with id: {}", link.getId());
        return "redirect:/links";
    }
}
