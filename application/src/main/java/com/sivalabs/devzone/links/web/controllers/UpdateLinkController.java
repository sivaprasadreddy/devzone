package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.UpdateLinkRequest;
import com.sivalabs.devzone.links.domain.services.LinkService;
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
public class UpdateLinkController {
    private static final String MODEL_ATTRIBUTE_LINK = "link";
    private static final Logger log = LoggerFactory.getLogger(UpdateLinkController.class);

    private final LinkService linkService;

    public UpdateLinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/links/{id}/edit")
    @AnyAuthenticatedUser
    public String editLinkForm(@PathVariable Long id, @CurrentUser User loginUser, Model model) {
        Link link = linkService.getLinkById(id).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found");
        }
        this.checkPrivilege(link, loginUser);
        String category = null;
        if (link.category() != null) {
            category = link.category().name();
        }
        UpdateLinkRequest updateLinkRequest =
                new UpdateLinkRequest(id, link.url(), link.title(), category);

        model.addAttribute(MODEL_ATTRIBUTE_LINK, updateLinkRequest);
        return "edit-link";
    }

    @PutMapping("/links/{id}")
    @AnyAuthenticatedUser
    public String updateBookmark(
            @PathVariable Long id,
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_LINK) UpdateLinkRequest request,
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
        if (bindingResult.hasErrors()) {
            return "edit-link";
        }
        Link link = linkService.getLinkById(id).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found");
        }
        var updateLinkRequest =
                new UpdateLinkRequest(id, request.url(), request.title(), request.category());
        this.checkPrivilege(link, loginUser);
        Link updatedLink = linkService.updateLink(updateLinkRequest);
        log.info("Link with id: {} updated successfully", updatedLink.id());
        return "redirect:/links";
    }

    private void checkPrivilege(Link link, User loginUser) {
        if (!(Objects.equals(link.createdBy().id(), loginUser.id())
                || loginUser.isAdminOrModerator())) {
            throw new UnauthorisedAccessException("Permission Denied");
        }
    }
}
