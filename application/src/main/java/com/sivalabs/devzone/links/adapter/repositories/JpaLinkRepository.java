package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.entities.LinkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    @Query(
            value = "select l from LinkEntity l JOIN FETCH l.category c join fetch l.createdBy u",
            countQuery = "select count(l) from LinkEntity l")
    Page<LinkEntity> findLinks(Pageable pageable);

    @Query(
            value =
                    "select l from LinkEntity l JOIN FETCH l.category c join fetch l.createdBy u"
                            + " where lower(l.title) like lower(concat('%', :query,'%'))",
            countQuery =
                    "select count(l) from LinkEntity l where lower(l.title) like lower(concat('%',"
                            + " :query,'%'))")
    Page<LinkEntity> findLinksByTitleContainingIgnoreCase(
            @Param("query") String query, Pageable pageable);

    @Query(
            value =
                    "select l from LinkEntity l join l.category c join fetch l.createdBy u where"
                            + " c.name=?1",
            countQuery = "select count(l) from LinkEntity l join l.category c where c.name=?1")
    Page<LinkEntity> findLinksByCategory(String categoryName, Pageable pageable);
}
