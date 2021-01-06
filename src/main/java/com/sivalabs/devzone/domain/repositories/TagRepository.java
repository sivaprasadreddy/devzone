package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tag);
}
