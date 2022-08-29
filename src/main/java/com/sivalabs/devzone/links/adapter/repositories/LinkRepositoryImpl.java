package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.adapter.entities.CategoryEntity;
import com.sivalabs.devzone.links.adapter.entities.LinkEntity;
import com.sivalabs.devzone.links.adapter.mappers.CategoryMapper;
import com.sivalabs.devzone.links.adapter.mappers.LinkMapper;
import com.sivalabs.devzone.links.domain.mappers.LinkDTOMapper;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinksDTO;
import com.sivalabs.devzone.links.domain.repositories.LinkRepository;
import com.sivalabs.devzone.users.adapter.entities.UserEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class LinkRepositoryImpl implements LinkRepository {
    private static final Integer PAGE_SIZE = 15;
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaLinkCreatorRepository jpaLinkCreatorRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final LinkMapper linkMapper;
    private final LinkDTOMapper linkDTOMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Link> findAll() {
        return jpaLinkRepository.findAll().stream().map(linkMapper::toModel).toList();
    }

    @Override
    public Optional<Link> findById(Long id) {
        return jpaLinkRepository.findById(id).map(linkMapper::toModel);
    }

    @Override
    public Link save(Link link) {
        LinkEntity entity = linkMapper.toEntity(link);
        UserEntity createdBy =
                jpaLinkCreatorRepository.findById(link.getCreatedBy().getId()).orElseThrow();
        CategoryEntity category = this.getOrCreateCategory(link.getCategory());
        entity.setCreatedBy(createdBy);
        entity.setCategory(category);
        var savedLink = jpaLinkRepository.save(entity);
        return linkMapper.toModel(savedLink);
    }

    @Override
    public LinksDTO getAllLinks(Integer page) {
        Pageable pageable = getPageable(page);
        Page<Long> pageOfLinkIds = jpaLinkRepository.findLinkIds(pageable);
        return getLinksDTO(pageable, pageOfLinkIds);
    }

    @Override
    public LinksDTO searchLinks(String query, Integer page) {
        Pageable pageable = getPageable(page);
        Page<Long> pageOfLinkIds =
                jpaLinkRepository.findLinkIdsByTitleContainingIgnoreCase(query, pageable);
        return getLinksDTO(pageable, pageOfLinkIds);
    }

    @Override
    public LinksDTO getLinksByCategory(String category, Integer page) {
        Optional<CategoryEntity> categoryOptional = jpaCategoryRepository.findByName(category);
        if (categoryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Category " + category + " not found");
        }

        Pageable pageable = getPageable(page);
        Page<Long> pageOfLinkIds = jpaLinkRepository.findLinkIdsByCategory(category, pageable);
        return getLinksDTO(pageable, pageOfLinkIds);
    }

    @Override
    public void deleteAll() {
        jpaLinkRepository.deleteAllInBatch();
    }

    @Override
    public void deleteById(Long id) {
        jpaLinkRepository.deleteById(id);
    }

    private static Pageable getPageable(Integer page) {
        int pageNo = page > 0 ? page - 1 : 0;
        return PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private LinksDTO getLinksDTO(Pageable pageable, Page<Long> pageOfLinkIds) {
        var links =
                jpaLinkRepository.findLinks(pageOfLinkIds.getContent(), pageable.getSort()).stream()
                        .map(linkMapper::toModel)
                        .toList();
        Page<Link> linksPage = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());

        return new LinksDTO(linksPage.map(linkDTOMapper::toDTO));
    }

    private CategoryEntity getOrCreateCategory(Category category) {
        Optional<CategoryEntity> categoryOptional =
                jpaCategoryRepository.findByName(category.getName());
        return categoryOptional.orElseGet(
                () -> jpaCategoryRepository.save(categoryMapper.toEntity(category)));
    }
}
