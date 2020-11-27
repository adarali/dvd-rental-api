package com.example.rest.dvdrental.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Schema(name = "Paginated Request", description = "Paginated request with filters")
public class LazyRequest {
    @Schema(minimum = "0", description = "The page to query, first page is 0", example = "2")
    private int page;
    @Schema(minimum = "1", description = "The maximum number of items to show per page", example = "10")
    private int pageSize = 10;
    @Schema(description = "The field to sort by", allowableValues = {"likes", "title"})
    private String sortField = "title";
    @Schema(description = "The sort direction. 1 for Ascendant. 0 for Descendant ", allowableValues = {"0", "1"}, example = "1")
    private int sortOrder = 1;
    @Schema(description = "Filter the list by availability or title. available accepts 3 values: 0, 1, 2.\n" +
            "1 is to show available movies only. 0 is to show unavailable movies. 2 is to show both available and unavailable movies",
            allowableValues = {"available", "title"}, example = "{\"available\": 1, \"title\": \"Hello\"}")
    private Map<String, Object> filters = new HashMap<>();
    
    public LazyRequest() {
        this.filters.put("available", 1);
        this.filters.put("title", "");
    }
    
    @JsonIgnore
    public PageRequest getPageRequest() {
        String sortField = this.sortField;
        if ("likes".equals(sortField)) {
            sortField = "likeCount";
        }
        Sort sort = Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        return PageRequest.of(page, pageSize, sort);
    }
    
}
