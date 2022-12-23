package com.sivalabs.devzone.posts.adapter.repositories;

import com.sivalabs.devzone.posts.adapter.entities.CategoryEntity;
import com.sivalabs.devzone.posts.domain.models.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    @Query(
            """
        select new com.sivalabs.devzone.posts.domain.models.Category(c.id, c.name)
        from CategoryEntity c
        """)
    List<Category> findAllCategories(Sort sort);

    @Modifying
    @Query(
            value = "insert into categories(name) values(:#{#c.name}) ON CONFLICT DO NOTHING",
            nativeQuery = true)
    void upsert(@Param("c") CategoryEntity category);
}
