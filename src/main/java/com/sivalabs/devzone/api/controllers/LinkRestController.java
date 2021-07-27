package com.sivalabs.devzone.api.controllers;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LinkRestController {
    private final LinkService linkService;
    private final SecurityService securityService;

    @GetMapping("/links")
    public LinksDTO home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @PageableDefault(size = 15)
                    @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)})
                    Pageable pageable) {
        LinksDTO data;
        if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, pageable.getPageNumber());
            data = linkService.getLinksByTag(tag, pageable);
        } else if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, pageable.getPageNumber());
            data = linkService.searchLinks(query, pageable);
        } else {
            log.info("Fetching links with page: {}", pageable.getPageNumber());
            data = linkService.getAllLinks(pageable);
        }
        return data;
    }

    @GetMapping("/links/{id}")
    public LinkDTO getLink(@PathVariable Long id) {
        return linkService.getLinkById(id).orElse(null);
    }

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.CREATED)
    @AnyAuthenticatedUser
    public void createLink(@Valid @RequestBody LinkDTO link, @CurrentUser User loginUser) {
        link.setCreatedUserId(loginUser.getId());
        linkService.createLink(link);
    }

    @PutMapping("/links/{id}")
    @AnyAuthenticatedUser
    public void updateLink(
            @PathVariable Long id, @Valid @RequestBody LinkDTO link, @CurrentUser User loginUser) {
        this.checkPrivilege(id, link, loginUser);
        link.setId(id);
        link.setCreatedUserId(loginUser.getId());
        linkService.updateLink(link);
    }

    @DeleteMapping("/links/{id}")
    @AnyAuthenticatedUser
    public ResponseEntity<Void> deleteLink(@PathVariable Long id, @CurrentUser User loginUser) {
        LinkDTO link = linkService.getLinkById(id).orElse(null);
        this.checkPrivilege(id, link, loginUser);
        linkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(Long linkId, LinkDTO link, User loginUser) {
        if (link == null
                || !(link.getCreatedUserId().equals(loginUser.getId())
                        || securityService.isCurrentUserAdmin())) {
            throw new ResourceNotFoundException("Link not found with id=" + linkId);
        }
    }
}
