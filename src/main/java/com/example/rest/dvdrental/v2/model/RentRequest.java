package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Rent Request", description = "This is the rent request for a movie")
public class RentRequest {
    @NotNull(message="The movie id was not specified")
    @Schema(description = "The ID of the movie to rent", example = "101")
    private Long movieId;
    @NotNull(message="The number of rental days was not specified")
    @Min(value=1, message = "The number of rent days must be greater than 0")
    @Schema(description = "The number of days to rent a movie", example = "3")
    private int rentDays = 1;
    @NotNull(message="The quantity was not specified")
    @Min(value=1, message = "The quantity must be greater than 0")
    @Schema(description = "The quantity to rent", example = "4")
    private int quantity = 1;
    
    public RentRequest(Long movieId, int rentDays, int quantity) {
        this.movieId = movieId;
        this.rentDays = rentDays;
        this.quantity = quantity;
    }
}
