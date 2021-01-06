package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Link;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("select l.id from Link l")
    Page<Long> fetchLinkIds(Pageable pageable);

    @Query("select l.id from Link l where lower(l.title) like lower(concat('%', :query,'%'))")
    Page<Long> fetchLinkIdsByTitleContainingIgnoreCase(
            @Param("query") String query, Pageable pageable);

    @Query("select l.id from Link l join l.tags t where t.name=?1")
    Page<Long> fetchLinkIdsByTag(String tagName, Pageable pageable);

    @Query(
            "select DISTINCT l from Link l JOIN FETCH l.tags join fetch l.createdBy where l.id in ?1")
    List<Link> findLinksWithTags(List<Long> linkIds, Sort sort);
}
