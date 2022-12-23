package com.sivalabs.devzone.links.web.mappers;

import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinkDTO;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.services.SecurityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class LinkDtoMapper {
    private final SecurityService securityService;

    public LinkDtoMapper(SecurityService securityService) {
        this.securityService = securityService;
    }

    public List<LinkDTO> toDTOs(List<Link> links) {
        if (links == null) {
            return new ArrayList<>(0);
        }
        User loginUser = securityService.loginUser();
        return links.stream().map(link -> this.toDTO(loginUser, link)).toList();
    }

    public LinkDTO toDTO(User loginUser, Link link) {
        String category = null;
        if (link.category() != null) {
            category = link.category().name();
        }
        boolean editable = this.canCurrentUserEditLink(loginUser, link);
        return new LinkDTO(
                link.id(),
                link.url(),
                link.title(),
                category,
                link.createdBy().id(),
                link.createdBy().name(),
                link.createdAt(),
                link.updatedAt(),
                editable);
    }

    public boolean canCurrentUserEditLink(User loginUser, Link link) {
        return loginUser != null
                && link != null
                && (Objects.equals(link.createdBy().id(), loginUser.id())
                        || loginUser.isAdminOrModerator());
    }
}
