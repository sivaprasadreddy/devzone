package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.entities.Category;
import com.sivalabs.devzone.domain.entities.Link;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.mappers.LinkMapper;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.repositories.LinkRepository;
import com.sivalabs.devzone.domain.repositories.UserRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final UserRepository userRepository;
    private final LinkMapper linkMapper;

    @Transactional(readOnly = true)
    public List<LinkDTO> getAllLinks() {
        return linkRepository.findAll().stream()
                .map(linkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LinksDTO getAllLinks(Integer page) {
        int pageNo = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Long> pageOfLinkIds = linkRepository.fetchLinkIds(pageable);
        List<Link> links =
                linkRepository.findLinksWithCategory(
                        pageOfLinkIds.getContent(), pageable.getSort());
        Page<Link> linksPage = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());
        return buildLinksResult(linksPage);
    }

    @Transactional(readOnly = true)
    public LinksDTO searchLinks(String query, Integer page) {
        int pageNo = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, 15, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Long> pageOfLinkIds =
                linkRepository.fetchLinkIdsByTitleContainingIgnoreCase(query, pageable);
        List<Link> links =
                linkRepository.findLinksWithCategory(
                        pageOfLinkIds.getContent(), pageable.getSort());
        Page<Link> linksPage = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());
        return buildLinksResult(linksPage);
    }

    @Transactional(readOnly = true)
    public LinksDTO getLinksByCategory(String category, Integer page) {
        int pageNo = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, 15, Sort.by(Sort.Direction.DESC, "createdAt"));

        Optional<Category> categoryOptional = categoryService.findCategoryByName(category);
        if (categoryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Category " + category + " not found");
        }
        Page<Long> pageOfLinkIds = linkRepository.fetchLinkIdsByCategory(category, pageable);
        List<Link> links =
                linkRepository.findLinksWithCategory(
                        pageOfLinkIds.getContent(), pageable.getSort());
        Page<Link> linksPage = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());
        return buildLinksResult(linksPage);
    }

    @Transactional(readOnly = true)
    public Optional<LinkDTO> getLinkById(Long id) {
        log.debug("process=get_link_by_id, id={}", id);
        return linkRepository.findById(id).map(linkMapper::toDTO);
    }

    public LinkDTO createLink(LinkDTO link) {
        link.setId(null);
        log.debug("process=create_link, url={}", link.getUrl());
        return linkMapper.toDTO(saveLink(link));
    }

    public LinkDTO updateLink(LinkDTO link) {
        log.debug("process=update_link, url={}", link.getUrl());
        return linkMapper.toDTO(saveLink(link));
    }

    public void deleteLink(Long id) {
        log.debug("process=delete_link_by_id, id={}", id);
        linkRepository.deleteById(id);
    }

    public void deleteAllLinks() {
        log.debug("process=delete_all_links");
        linkRepository.deleteAllInBatch();
    }

    private LinksDTO buildLinksResult(Page<Link> links) {
        log.trace("Found {} links in page", links.getNumberOfElements());
        return new LinksDTO(links.map(linkMapper::toDTO));
    }

    private Link saveLink(LinkDTO linkDTO) {
        Link link;
        if (linkDTO.getId() == null || linkDTO.getId() == 0) {
            link = new Link();
            link.setCreatedBy(userRepository.getReferenceById(linkDTO.getCreatedUserId()));
        } else {
            link =
                    linkRepository
                            .findById(linkDTO.getId())
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "Link with id: "
                                                            + linkDTO.getId()
                                                            + " not found"));
        }
        link.setUrl(linkDTO.getUrl());
        link.setTitle(getTitle(linkDTO));
        Category category = this.getOrCreateCategory(linkDTO.getCategory().trim());
        link.setCategory(category);
        return linkRepository.save(link);
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
        Optional<Category> categoryOptional = categoryService.findCategoryByName(name);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        Category category = new Category();
        category.setName(name);
        return categoryService.createCategory(category);
    }

    public static String toSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
