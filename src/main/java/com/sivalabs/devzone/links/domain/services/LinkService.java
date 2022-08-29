package com.sivalabs.devzone.links.domain.services;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinksDTO;
import com.sivalabs.devzone.links.domain.models.UpdateLinkRequest;
import com.sivalabs.devzone.links.domain.repositories.CategoryRepository;
import com.sivalabs.devzone.links.domain.repositories.LinkRepository;
import com.sivalabs.devzone.links.domain.utils.JsoupUtils;
import com.sivalabs.devzone.users.domain.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sivalabs.devzone.links.domain.utils.StringUtils.toSlug;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkService {
    private final CategoryRepository categoryRepository;
    private final LinkRepository linkRepository;

    @Transactional(readOnly = true)
    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LinksDTO getAllLinks(Integer page) {
        return linkRepository.getAllLinks(page);
    }

    @Transactional(readOnly = true)
    public LinksDTO searchLinks(String query, Integer page) {
        return linkRepository.searchLinks(query, page);
    }

    @Transactional(readOnly = true)
    public LinksDTO getLinksByCategory(String category, Integer page) {
        return linkRepository.getLinksByCategory(category, page);
    }

    @Transactional(readOnly = true)
    public Optional<Link> getLinkById(Long id) {
        log.debug("process=get_link_by_id, id={}", id);
        return linkRepository.findById(id);
    }

    public Link createLink(CreateLinkRequest createLinkRequest) {
        Link link = new Link();
        link.setUrl(createLinkRequest.getUrl());
        link.setTitle(getTitle(createLinkRequest.getUrl(), createLinkRequest.getTitle()));
        Category category = this.buildCategory(createLinkRequest.getCategory());
        link.setCategory(category);
        User user = new User();
        user.setId(createLinkRequest.getCreatedUserId());
        link.setCreatedBy(user);
        log.debug("process=create_link, url={}", link.getUrl());
        link.setTitle(getTitle(link.getUrl(), link.getTitle()));
        return linkRepository.save(link);
    }

    public Link updateLink(UpdateLinkRequest updateLinkRequest) {
        Link link =
                linkRepository
                        .findById(updateLinkRequest.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Link with id: "
                                                        + updateLinkRequest.getId()
                                                        + " not found"));
        link.setUrl(updateLinkRequest.getUrl());
        link.setTitle(getTitle(updateLinkRequest.getUrl(), updateLinkRequest.getTitle()));
        Category category = this.buildCategory(updateLinkRequest.getCategory());
        link.setCategory(category);
        log.debug("process=update_link, url={}", link.getUrl());
        return linkRepository.save(link);
    }

    public void deleteLink(Long id) {
        log.debug("process=delete_link_by_id, id={}", id);
        linkRepository.deleteById(id);
    }

    public void deleteAllLinks() {
        log.debug("process=delete_all_links");
        linkRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    private String getTitle(String utl, String title) {
        if (StringUtils.isNotEmpty(title)) {
            return title;
        }
        return JsoupUtils.getTitle(utl);
    }

    private Category buildCategory(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return null;
        }
        String name = toSlug(categoryName.trim());
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
