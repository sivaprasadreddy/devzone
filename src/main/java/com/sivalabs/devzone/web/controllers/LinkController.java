package com.sivalabs.devzone.web.controllers;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.annotations.CurrentUser;
import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
import com.sivalabs.devzone.domain.services.TagService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LinkController {
    private static final String PAGINATION_PREFIX = "paginationPrefix";
    private static final String MODEL_ATTRIBUTE_LINK = "link";

    private final LinkService linkService;
    private final TagService tagService;
    private final SecurityService securityService;

    @ModelAttribute("tags")
    public List<Tag> allTags() {
        return tagService.findAllTags();
    }

    @GetMapping("/links")
    public String home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @PageableDefault(size = 15)
                    @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)})
                    Pageable pageable,
            Model model) {
        LinksDTO data;
        if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, pageable.getPageNumber());
            data = linkService.getLinksByTag(tag, pageable);
            model.addAttribute("header", "Links by Tag : " + tag);
            model.addAttribute(PAGINATION_PREFIX, "/links?tag=" + tag);
        } else if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, pageable.getPageNumber());
            data = linkService.searchLinks(query, pageable);
            model.addAttribute("header", "Search Results for : " + query);
            model.addAttribute(PAGINATION_PREFIX, "/links?query=" + query);
        } else {
            log.info("Fetching links with page: {}", pageable.getPageNumber());
            data = linkService.getAllLinks(pageable);
            model.addAttribute(PAGINATION_PREFIX, "/links?");
        }
        model.addAttribute("linksData", data);
        return "links";
    }

    @GetMapping("/links/new")
    @AnyAuthenticatedUser
    public String newLinkForm(Model model) {
        model.addAttribute(MODEL_ATTRIBUTE_LINK, new LinkDTO());
        return "add-link";
    }

    @PostMapping("/links")
    @AnyAuthenticatedUser
    public String createLink(
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_LINK) LinkDTO link,
            @CurrentUser User loginUser,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-link";
        }
        link.setCreatedUserId(loginUser.getId());
        linkService.createLink(link);
        return "redirect:/links";
    }

    @GetMapping("/links/edit/{id}")
    @AnyAuthenticatedUser
    public String editLinkForm(@PathVariable Long id, @CurrentUser User loginUser, Model model) {
        LinkDTO link = linkService.getLinkById(id).orElse(null);
        this.checkPrivilege(id, link, loginUser);
        model.addAttribute(MODEL_ATTRIBUTE_LINK, link);
        return "edit-link";
    }

    @PutMapping("/links/{id}")
    @AnyAuthenticatedUser
    public String updateBookmark(
            @PathVariable Long id,
            @Valid @ModelAttribute(MODEL_ATTRIBUTE_LINK) LinkDTO link,
            @CurrentUser User loginUser,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-link";
        }
        link.setId(id);
        link.setCreatedUserId(loginUser.getId());
        this.checkPrivilege(id, link, loginUser);
        linkService.updateLink(link);
        return "redirect:/links";
    }

    @DeleteMapping("/links/{id}")
    @ResponseStatus
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
