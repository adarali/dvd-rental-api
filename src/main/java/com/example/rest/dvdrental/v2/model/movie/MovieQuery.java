package com.example.rest.dvdrental.v2.model.movie;

import com.example.rest.dvdrental.v2.model.LazyRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter @Setter
@Schema(description = "The query string parameters (The parameters you type into the URL)")
public class MovieQuery {
    @Schema(description = "The number of the page to show")
    private int page;
    @Schema(description = "The number of records to show per page")
    @Min(value=1, message = "Value must be more or equal to 1")
    private int pageSize = 10;
    @Schema(description = "Filter movies by title containing this value")
    private String title;
    @Schema(description = "Filter movies by availability", allowableValues = {"0", "1", "2"})
    private int available = 1;
    @Schema(description = "Sort movies by the value of this field. if you append a '-' before the name of the field, the records become sorted in descending order", allowableValues = {"title", "likes"})
    private String sort;
    
    public LazyRequest toLazyRequest() {
        LazyRequest request = new LazyRequest();
        request.setPage(page);
        request.setPageSize(pageSize);
        if (!StringUtils.isBlank(title)) {
            request.getFilters().put("title", title);
        }
        request.getFilters().put("available", available);
        String sort = StringUtils.removeStart(this.sort, "-");
        if (sort != null) {
            request.setSortField(sort);
            if(this.sort.startsWith("-")) {
                request.setSortOrder(0);
            }
        }
        return request;
    }
}
