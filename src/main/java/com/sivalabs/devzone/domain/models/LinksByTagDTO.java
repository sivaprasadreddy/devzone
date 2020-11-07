package com.sivalabs.devzone.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinksByTagDTO {
    private Long id;
    private String name;
    private List<LinkDTO> links;
}
