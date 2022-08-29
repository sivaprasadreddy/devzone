package com.sivalabs.devzone.links.adapter.repositories;

import com.sivalabs.devzone.links.adapter.entities.CategoryEntity;
import com.sivalabs.devzone.links.domain.models.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    @Query(
            """
        select new com.sivalabs.devzone.links.domain.models.Category(c.id, c.name)
        from CategoryEntity c
        """)
    List<Category> findAllCategories(Sort sort);
}
