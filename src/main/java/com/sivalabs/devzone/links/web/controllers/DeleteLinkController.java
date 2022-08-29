package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.services.LinkService;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.services.SecurityService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DeleteLinkController {

    private final LinkService linkService;
    private final SecurityService securityService;

    @DeleteMapping("/links/{id}")
    @ResponseStatus
    @AnyAuthenticatedUser
    public ResponseEntity<Void> deleteLink(@PathVariable Long id, @CurrentUser User loginUser) {
        Link link = linkService.getLinkById(id).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found");
        }
        this.checkPrivilege(link, loginUser);
        linkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(Link link, User loginUser) {
        if (!(Objects.equals(link.getCreatedBy().getId(), loginUser.getId())
                || securityService.isUserAdminOrModerator(loginUser))) {
            throw new UnauthorisedAccessException("Permission Denied");
        }
    }
}
