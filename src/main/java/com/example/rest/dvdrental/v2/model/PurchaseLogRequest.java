package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(name = "Purchase Log Request", description = "The request to query the purchase logs")
@Getter
@Setter
public class PurchaseLogRequest {
    @Schema(description = "The ID of the purchased movie", example = "99")
    private Long movieId;
    @Schema(description = "Search movies of which their title contains this property's value", example = "Hello World!")
    private String movieTitle;
    @Schema(description = "The username of the user who purchased the movie", example = "user007")
    private String username;
    @Schema(description = "Filter by users whose full name contains this value", example = "Michael Jackson")
    private String userFullName;
    @Schema(description = "The initial date of the date range to filter by purchase date", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @Schema(description = "The final date of the date range to filter by purchase date", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
}
