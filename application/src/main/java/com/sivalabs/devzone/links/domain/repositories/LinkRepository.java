package com.sivalabs.devzone.links.domain.repositories;

import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.links.domain.models.Link;
import java.util.Optional;

public interface LinkRepository {
    PagedResult<Link> getAllLinks(Integer page);

    PagedResult<Link> searchLinks(String query, Integer page);

    PagedResult<Link> getLinksByCategory(String category, Integer page);

    Optional<Link> findById(Long id);

    Link save(Link link);

    void deleteAll();

    void deleteById(Long id);
}
