package com.sivalabs.devzone.links.web.mappers;

import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinkDTO;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.services.SecurityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkDtoMapper {
    private final SecurityService securityService;

    public List<LinkDTO> toDTOs(List<Link> links) {
        if (links == null) {
            return new ArrayList<>(0);
        }
        User loginUser = securityService.loginUser();
        return links.stream().map(link -> this.toDTO(loginUser, link)).toList();
    }

    public LinkDTO toDTO(User loginUser, Link link) {
        LinkDTO dto = new LinkDTO();
        dto.setId(link.getId());
        dto.setUrl(link.getUrl());
        dto.setTitle(link.getTitle());
        dto.setCreatedUserId(link.getCreatedBy().getId());
        dto.setCreatedUserName(link.getCreatedBy().getName());
        dto.setCreatedAt(link.getCreatedAt());
        dto.setUpdatedAt(link.getUpdatedAt());
        if (link.getCategory() != null) {
            dto.setCategory(link.getCategory().getName());
        }

        boolean editable = this.canCurrentUserEditLink(loginUser, dto);
        dto.setEditable(editable);
        return dto;
    }

    public boolean canCurrentUserEditLink(User loginUser, LinkDTO linkDTO) {
        return loginUser != null
                && linkDTO != null
                && (Objects.equals(linkDTO.getCreatedUserId(), loginUser.getId())
                        || loginUser.isAdminOrModerator());
    }
}
