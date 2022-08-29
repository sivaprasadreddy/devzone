package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.entities.LinkEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    @Query("select l.id from LinkEntity l")
    Page<Long> findLinkIds(Pageable pageable);

    @Query("select l.id from LinkEntity l where lower(l.title) like lower(concat('%', :query,'%'))")
    Page<Long> findLinkIdsByTitleContainingIgnoreCase(
            @Param("query") String query, Pageable pageable);

    @Query("select l.id from LinkEntity l join l.category c where c.name=?1")
    Page<Long> findLinkIdsByCategory(String categoryName, Pageable pageable);

    @Query(
            """
            select DISTINCT l
            from LinkEntity l JOIN FETCH l.category join fetch l.createdBy
            where l.id in ?1
        """)
    List<LinkEntity> findLinks(List<Long> linkIds, Sort sort);
}
