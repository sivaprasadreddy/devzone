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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UpdateLinkController {
    private static final String MODEL_ATTRIBUTE_LINK = "link";

    private final LinkService linkService;

    @GetMapping("/links/{id}/edit")
    @AnyAuthenticatedUser
    public String editLinkForm(@PathVariable Long id, @CurrentUser User loginUser, Model model) {
        Link link = linkService.getLinkById(id).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found");
        }
        this.checkPrivilege(link, loginUser);
        UpdateLinkRequest updateLinkRequest = new UpdateLinkRequest();
        updateLinkRequest.setId(id);
        updateLinkRequest.setUrl(link.getUrl());
        updateLinkRequest.setTitle(link.getTitle());
        if (link.getCategory() != null) {
            updateLinkRequest.setCategory(link.getCategory().getName());
        }
        model.addAttribute(MODEL_ATTRIBUTE_LINK, updateLinkRequest);
        return "edit-link";
    }

    @PutMapping("/links/{id}")
    @AnyAuthenticatedUser
    public String updateBookmark(
            @PathVariable Long id,
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_LINK) UpdateLinkRequest updateLinkRequest,
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
        if (bindingResult.hasErrors()) {
            return "edit-link";
        }
        Link link = linkService.getLinkById(id).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found");
        }
        updateLinkRequest.setId(id);
        this.checkPrivilege(link, loginUser);
        Link updatedLink = linkService.updateLink(updateLinkRequest);
        log.info("Link with id: {} updated successfully", updatedLink.getId());
        return "redirect:/links";
    }

    private void checkPrivilege(Link link, User loginUser) {
        if (!(Objects.equals(link.getCreatedBy().getId(), loginUser.getId())
                || loginUser.isAdminOrModerator())) {
            throw new UnauthorisedAccessException("Permission Denied");
        }
    }
}
