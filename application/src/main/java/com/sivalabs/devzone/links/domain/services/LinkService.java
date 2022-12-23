package com.sivalabs.devzone.links.domain.services;

import static com.sivalabs.devzone.links.domain.utils.StringUtils.toSlug;

import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.UpdateLinkRequest;
import com.sivalabs.devzone.links.domain.repositories.CategoryRepository;
import com.sivalabs.devzone.links.domain.repositories.LinkRepository;
import com.sivalabs.devzone.links.domain.utils.JsoupUtils;
import com.sivalabs.devzone.users.domain.models.User;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LinkService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LinkService.class);
    private final CategoryRepository categoryRepository;
    private final LinkRepository linkRepository;

    public LinkService(CategoryRepository categoryRepository, LinkRepository linkRepository) {
        this.categoryRepository = categoryRepository;
        this.linkRepository = linkRepository;
    }

    @Transactional(readOnly = true)
    public PagedResult<Link> getLinks(Integer page) {
        log.debug("process=get_links, page={}", page);
        return linkRepository.getAllLinks(page);
    }

    @Transactional(readOnly = true)
    public PagedResult<Link> searchLinks(String query, Integer page) {
        log.debug("process=search_links, query={}, page={}", query, page);
        return linkRepository.searchLinks(query, page);
    }

    @Transactional(readOnly = true)
    public PagedResult<Link> getLinksByCategory(String category, Integer page) {
        log.debug("process=get_links_by_category, category={}, page={}", category, page);
        return linkRepository.getLinksByCategory(category, page);
    }

    @Transactional(readOnly = true)
    public Optional<Link> getLinkById(Long id) {
        log.debug("process=get_link_by_id, id={}", id);
        return linkRepository.findById(id);
    }

    public Link createLink(CreateLinkRequest createLinkRequest) {
        log.debug("process=create_link, url={}", createLinkRequest.getUrl());
        Link link = new Link();
        link.setUrl(createLinkRequest.getUrl());
        link.setTitle(getTitle(createLinkRequest.getUrl(), createLinkRequest.getTitle()));
        Category category = this.buildCategory(createLinkRequest.getCategory());
        link.setCategory(category);
        User user = new User();
        user.setId(createLinkRequest.getCreatedUserId());
        link.setCreatedBy(user);
        link.setTitle(getTitle(link.getUrl(), link.getTitle()));
        return linkRepository.save(link);
    }

    public Link updateLink(UpdateLinkRequest updateLinkRequest) {
        log.debug("process=update_link, id={}", updateLinkRequest.getId());
        Link link = linkRepository.findById(updateLinkRequest.getId()).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException(
                    "Link with id: " + updateLinkRequest.getId() + " not found");
        }
        link.setUrl(updateLinkRequest.getUrl());
        link.setTitle(getTitle(updateLinkRequest.getUrl(), updateLinkRequest.getTitle()));
        Category category = this.buildCategory(updateLinkRequest.getCategory());
        link.setCategory(category);
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

    private String getTitle(String url, String title) {
        if (StringUtils.isNotEmpty(title)) {
            return title;
        }
        return JsoupUtils.getTitle(url);
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
