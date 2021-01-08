package com.sivalabs.devzone.domain.repositories;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_IT;
import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.devzone.domain.entities.Link;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=NONE",
            "spring.datasource.url=jdbc:tc:postgresql:12.3:///testdb"
        })
@ActiveProfiles(PROFILE_IT)
class LinkRepositoryIT {

    @Autowired private LinkRepository linkRepository;

    @Test
    void shouldReturnAllLinks() {
        List<Link> allLinks = linkRepository.findAll();
        assertThat(allLinks).isNotNull();
    }
}
