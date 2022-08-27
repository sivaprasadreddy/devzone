package com.sivalabs.devzone.links.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinksDTO {
    private List<LinkDTO> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;

    @JsonProperty("isFirst")
    private boolean isFirst;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    public LinksDTO(Page<LinkDTO> linksPage) {
        this.setData(linksPage.getContent());
        this.setTotalElements(linksPage.getTotalElements());
        this.setPageNumber(linksPage.getNumber() + 1); // 1 - based page numbering
        this.setTotalPages(linksPage.getTotalPages());
        this.setFirst(linksPage.isFirst());
        this.setHasNext(linksPage.hasNext());
        this.setHasPrevious(linksPage.hasPrevious());
    }
}
