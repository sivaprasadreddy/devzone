package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("select distinct l from Link l join l.tags t where t.name=?1")
    Page<Link> findByTag(String tagName, Pageable pageable);

    Page<Link> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}
