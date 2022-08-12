package com.sivalabs.devzone.web.controllers;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.config.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.annotations.CurrentUser;
import com.sivalabs.devzone.domain.entities.Category;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.CategoryService;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
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
    private final CategoryService categoryService;
    private final SecurityService securityService;

    @ModelAttribute("categories")
    public List<Category> allCategories() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/links")
    public String home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "category", required = false) String category,
            @PageableDefault(size = 15)
                    @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)})
                    Pageable pageable,
            Model model) {
        LinksDTO data;
        if (StringUtils.isNotEmpty(category)) {
            log.info(
                    "Fetching links for category {} with page: {}",
                    category,
                    pageable.getPageNumber());
            data = linkService.getLinksByCategory(category, pageable);
            model.addAttribute("header", "Links by Category : " + category);
            model.addAttribute(PAGINATION_PREFIX, "/links?category=" + category);
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
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
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
            BindingResult bindingResult,
            @CurrentUser User loginUser) {
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
                        || securityService.isUserAdminOrModerator(loginUser))) {
            throw new ResourceNotFoundException("Link not found with id=" + linkId);
        }
    }
}
