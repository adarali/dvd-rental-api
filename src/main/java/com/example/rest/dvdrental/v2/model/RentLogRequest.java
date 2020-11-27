package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(name = "Rent Log Request", description = "The request to query the rent logs")
@Getter
@Setter
public class RentLogRequest {
    @Schema(description = "The ID of the rented movie", example = "99")
    private Long movieId;
    @Schema(description = "Search movies of which their title contains this property's value", example = "Hello World!")
    private String movieTitle;
    @Schema(description = "The username of the user who rented the movie", example = "user007")
    private String username;
    @Schema(description = "Filter by users whose full name contains this value", example = "Michael Jackson")
    private String userFullName;
    @Schema(description = "The initial date of the date range to filter by rent date", required = true)
    private LocalDate dateFrom;
    @Schema(description = "The final date of the date range to filter by rent date", required = true)
    private LocalDate dateTo;
    @Schema(description = "If the rent is returned or not. You can omit this filter")
    private Boolean returned;
    
    
}
