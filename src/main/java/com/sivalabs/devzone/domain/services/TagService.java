package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.repositories.TagRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    @Cacheable("tags")
    public List<Tag> findAllTags() {
        Sort sort = Sort.by("name");
        return tagRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    @Cacheable("tag-by-name")
    public Optional<Tag> findTagByName(String tag) {
        return tagRepository.findByName(tag);
    }

    @CacheEvict(
            cacheNames = {"tags", "tag-by-name"},
            allEntries = true)
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }
}
