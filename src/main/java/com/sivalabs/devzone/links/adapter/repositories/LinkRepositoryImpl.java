package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.entities.CategoryEntity;
import com.sivalabs.devzone.links.adapter.entities.LinkEntity;
import com.sivalabs.devzone.links.adapter.mappers.CategoryMapper;
import com.sivalabs.devzone.links.adapter.mappers.LinkMapper;
import com.sivalabs.devzone.links.domain.models.Category;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.repositories.LinkRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class LinkRepositoryImpl implements LinkRepository {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaLinkCreatorRepository jpaLinkCreatorRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final LinkMapper linkMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<Long> fetchLinkIds(Pageable pageable) {
        return jpaLinkRepository.fetchLinkIds(pageable);
    }

    @Override
    public Page<Long> fetchLinkIdsByTitleContainingIgnoreCase(String query, Pageable pageable) {
        return jpaLinkRepository.fetchLinkIdsByTitleContainingIgnoreCase(query, pageable);
    }

    @Override
    public Page<Long> fetchLinkIdsByCategory(String categoryName, Pageable pageable) {
        return jpaLinkRepository.fetchLinkIdsByCategory(categoryName, pageable);
    }

    @Override
    public List<Link> findLinksWithCategory(List<Long> linkIds, Sort sort) {
        return jpaLinkRepository.findLinksWithCategory(linkIds, sort).stream()
                .map(linkMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Link> findById(Long id) {
        return jpaLinkRepository.findById(id).map(linkMapper::toModel);
    }

    @Override
    public void deleteAllInBatch() {
        jpaLinkRepository.deleteAllInBatch();
    }

    @Override
    public void deleteById(Long id) {
        jpaLinkRepository.deleteById(id);
    }

    @Override
    public Link save(Link link) {
        LinkEntity entity = linkMapper.toEntity(link);
        entity.setCreatedBy(
                jpaLinkCreatorRepository.findById(link.getCreatedBy().getId()).orElseThrow());
        CategoryEntity category = this.getOrCreateCategory(link.getCategory());
        entity.setCategory(category);
        var savedLink = jpaLinkRepository.save(entity);
        return linkMapper.toModel(savedLink);
    }

    @Override
    public List<Link> findAll() {
        return jpaLinkRepository.findAll().stream().map(linkMapper::toModel).toList();
    }

    private CategoryEntity getOrCreateCategory(Category category) {
        Optional<CategoryEntity> categoryOptional =
                jpaCategoryRepository.findByName(category.getName());
        return categoryOptional.orElseGet(
                () -> jpaCategoryRepository.save(categoryMapper.toEntity(category)));
    }
}
