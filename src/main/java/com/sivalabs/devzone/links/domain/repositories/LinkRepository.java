package com.sivalabs.devzone.links.domain.repositories;

import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinksDTO;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll();

    LinksDTO getAllLinks(Integer page);

    LinksDTO searchLinks(String query, Integer page);

    LinksDTO getLinksByCategory(String category, Integer page);

    Optional<Link> findById(Long id);

    Link save(Link link);

    void deleteAll();

    void deleteById(Long id);
}
