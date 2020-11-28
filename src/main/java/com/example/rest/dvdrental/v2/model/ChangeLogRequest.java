package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name="Change Log Query", description = "The request to query logs by movie, date range and the type of the change")
public class ChangeLogRequest {
    @Schema(description = "The ID of the movie", example = "20")
    private Long movieId;
    @Schema(description = "The initial date of the date range", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @Schema(description = "The final date of the date range", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
    @Schema(description = "The type of the change. if the value is different from the allowed values, then this query parameter is omitted", example = "SALE_PRICE", allowableValues = {"TITLE", "SALE_PRICE", "RENTAL_PRICE"})
    private String changeType;
}
