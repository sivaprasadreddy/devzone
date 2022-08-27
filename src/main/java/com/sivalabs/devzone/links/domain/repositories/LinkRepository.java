package com.sivalabs.devzone.links.domain.repositories;

import com.sivalabs.devzone.links.domain.models.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface LinkRepository {

    Page<Long> fetchLinkIds(Pageable pageable);

    Page<Long> fetchLinkIdsByTitleContainingIgnoreCase(String query, Pageable pageable);

    Page<Long> fetchLinkIdsByCategory(String categoryName, Pageable pageable);

    List<Link> findLinksWithCategory(List<Long> linkIds, Sort sort);

    Optional<Link> findById(Long id);

    void deleteAllInBatch();

    void deleteById(Long id);

    Link save(Link link);

    List<Link> findAll();
}
