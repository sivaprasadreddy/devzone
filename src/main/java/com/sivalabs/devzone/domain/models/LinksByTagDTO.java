package com.sivalabs.devzone.domain.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinksByTagDTO {
    private Long id;
    private String name;
    private List<LinkDTO> links;
}
