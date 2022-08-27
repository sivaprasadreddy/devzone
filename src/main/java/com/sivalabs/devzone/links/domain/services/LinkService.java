package com.sivalabs.devzone.links.domain.services;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.domain.mappers.LinkDTOMapper;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinkDTO;
import com.sivalabs.devzone.links.domain.models.LinksDTO;
import com.sivalabs.devzone.links.domain.repositories.LinkRepository;
import com.sivalabs.devzone.users.domain.models.User;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkService {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s");

    private final CategoryService categoryService;
    private final LinkRepository linkRepository;
    private final LinkDTOMapper linkDTOMapper;

    @Transactional(readOnly = true)
    public List<LinkDTO> getAllLinks() {
        return linkRepository.findAll().stream()
                .map(linkDTOMapper::toDTO)
                .collect(Collectors.toList());
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
    public Optional<LinkDTO> getLinkById(Long id) {
        log.debug("process=get_link_by_id, id={}", id);
        return linkRepository.findById(id).map(linkDTOMapper::toDTO);
    }

    public LinkDTO createLink(LinkDTO linkDTO) {
        Link link = new Link();
        link.setUrl(linkDTO.getUrl());
        link.setTitle(getTitle(linkDTO));
        User user = new User();
        user.setId(linkDTO.getCreatedUserId());
        link.setCreatedBy(user);
        Category category = this.getOrCreateCategory(linkDTO.getCategory().trim());
        link.setCategory(category);
        log.debug("process=create_link, url={}", link.getUrl());
        return linkDTOMapper.toDTO(linkRepository.save(link));
    }

    public LinkDTO updateLink(LinkDTO linkDTO) {
        Link link =
                linkRepository
                        .findById(linkDTO.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Link with id: " + linkDTO.getId() + " not found"));
        link.setUrl(linkDTO.getUrl());
        link.setTitle(getTitle(linkDTO));
        Category category = this.getOrCreateCategory(linkDTO.getCategory().trim());
        link.setCategory(category);
        log.debug("process=update_link, url={}", link.getUrl());
        return linkDTOMapper.toDTO(linkRepository.save(link));
    }

    public void deleteLink(Long id) {
        log.debug("process=delete_link_by_id, id={}", id);
        linkRepository.deleteById(id);
    }

    public void deleteAllLinks() {
        log.debug("process=delete_all_links");
        linkRepository.deleteAll();
    }

    private LinksDTO buildLinksResult(Page<Link> links) {
        log.trace("Found {} links in page", links.getNumberOfElements());
        return new LinksDTO(links.map(linkDTOMapper::toDTO));
    }

    private String getTitle(LinkDTO link) {
        if (StringUtils.isNotEmpty(link.getTitle())) {
            return link.getTitle();
        }
        try {
            Document doc = Jsoup.connect(link.getUrl()).get();
            return doc.title();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return link.getUrl();
    }

    private Category getOrCreateCategory(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return null;
        }
        String name = toSlug(categoryName.trim());
        Category category = new Category();
        category.setName(name);
        return category;
    }

    public static String toSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
