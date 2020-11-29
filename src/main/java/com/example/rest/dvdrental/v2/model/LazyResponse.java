package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Paginated Response")
public class LazyResponse<E> {
    @Schema(description = "The content of the current page", example = "[{\"id\":317,\"title\":\"Aaron Jackson\",\"likes\":6,\"available\":true},{\"id\":376,\"title\":\"Abderrazak Messchendorp\",\"likes\":4,\"available\":true},{\"id\":480,\"title\":\"Abigail Cook\",\"likes\":9,\"available\":false}]")
    private List<E> data = new ArrayList<>();
    @Schema(description = "The total number of records of all pages", example = "500")
    private Long totalRecords = 0L;
    
    public LazyResponse(List<E> data, Long totalRecords) {
        this.data = data;
        this.totalRecords = totalRecords;
    }
    
    public <T> LazyResponse<T> convertData(Function<E, T> function) {
        return new LazyResponse<>(data.stream().map(d -> function.apply(d)).collect(Collectors.toList()), totalRecords);
    }
}
