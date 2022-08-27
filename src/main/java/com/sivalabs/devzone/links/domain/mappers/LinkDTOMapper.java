package com.sivalabs.devzone.links.domain.mappers;

import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.links.domain.models.LinkDTO;
import com.sivalabs.devzone.users.domain.models.User;
import com.sivalabs.devzone.users.domain.services.SecurityService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkDTOMapper {
    private final SecurityService securityService;

    public LinkDTO toDTO(Link link) {
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
        boolean editable = this.canCurrentUserEditLink(dto);
        dto.setEditable(editable);
        return dto;
    }

    public boolean canCurrentUserEditLink(LinkDTO linkDTO) {
        User loginUser = securityService.loginUser();
        return loginUser != null
                && linkDTO != null
                && (Objects.equals(linkDTO.getCreatedUserId(), loginUser.getId())
                        || securityService.isUserAdminOrModerator(loginUser));
    }
}
