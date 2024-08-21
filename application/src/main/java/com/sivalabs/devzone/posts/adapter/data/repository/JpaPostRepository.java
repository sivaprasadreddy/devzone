package com.sivalabs.devzone.posts.adapter.data.repository;

import com.sivalabs.devzone.posts.adapter.data.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface JpaPostRepository extends JpaRepository<PostEntity, Long> {

    @Query(
            value = "select l from PostEntity l JOIN FETCH l.category c join fetch l.createdBy u",
            countQuery = "select count(l) from PostEntity l")
    Page<PostEntity> findPosts(Pageable pageable);

    @Query(
            value =
                    "select l from PostEntity l JOIN FETCH l.category c join fetch l.createdBy u"
                            + " where lower(l.title) like lower(concat('%', :query,'%'))",
            countQuery =
                    "select count(l) from PostEntity l where lower(l.title) like lower(concat('%',"
                            + " :query,'%'))")
    Page<PostEntity> findPostsByTitleContainingIgnoreCase(
            @Param("query") String query, Pageable pageable);

    @Query(
            value =
                    "select l from PostEntity l join l.category c join fetch l.createdBy u where"
                            + " c.name=?1",
            countQuery = "select count(l) from PostEntity l join l.category c where c.name=?1")
    Page<PostEntity> findPostsByCategory(String categoryName, Pageable pageable);
}
