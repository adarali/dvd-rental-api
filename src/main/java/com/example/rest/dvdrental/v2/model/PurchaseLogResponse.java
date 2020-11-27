package com.example.rest.dvdrental.v2.model;

import com.example.rest.dvdrental.v2.entities.Purchase;
import com.example.rest.dvdrental.v2.model.movie.MovieListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "Purchase Log Response", description = "This is the purchase log query result item")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseLogResponse {
    @Schema(description = "The purchased movie")
    private MovieListItem movie;
    @Schema(description = "The user who purchased the movie")
    private UserResponse user;
    @Schema(description = "The date and time of the purchase")
    private LocalDateTime purchaseDateTime = LocalDateTime.now();
    @Schema(description = "The price of the purchase", example = "25.00")
    private BigDecimal price;
    @Schema(description = "The number of purchased copies", example = "4")
    private BigDecimal quantity;
    
    public PurchaseLogResponse(Purchase purchase) {
        this.movie = MovieListItem.from(purchase.getMovie());
        this.user = new UserResponse(purchase.getUser());
        this.purchaseDateTime = purchase.getPurchaseDateTime();
        this.price = purchase.getPrice();
        this.quantity = purchase.getQuantity();
    }
    
}
