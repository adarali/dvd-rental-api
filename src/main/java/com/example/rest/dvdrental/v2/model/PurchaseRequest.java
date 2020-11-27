package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "The purchase request", name = "Purchase Request")
public class PurchaseRequest {
    @Schema(description = "The ID of the movie to purchase", example = "420")
    private Long movieId;
    @Schema(description = "The quantity to purchase", example = "2")
    private int quantity = 1;
    
    public PurchaseRequest(Long movieId, int quantity) {
        this.movieId = movieId;
        this.quantity = quantity;
    }
}
