package com.example.rest.dvdrental.v2.model;

import com.example.rest.dvdrental.v2.entities.Rent;
import com.example.rest.dvdrental.v2.model.movie.MovieListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Rent Log Response", description = "This is the rent log query result item")
@Getter
@Setter
@NoArgsConstructor
public class RentLogResponse {
    @Schema(description = "The ID of the rent")
    private Long id;
    @Schema(description = "The rented movie")
    private MovieListItem movie;
    @Schema(description = "The user who rented the movie")
    private UserResponse user;
    @Schema(description = "The date on which the movie was rented")
    private LocalDate rentDate;
    @Schema(description = "The expected return date of the rent")
    private LocalDate expectedReturnDate;
    @Schema(description = "The actual return date of the rent")
    private LocalDate actualReturnDate;
    @Schema(description = "If the rent was returned or not")
    private boolean returned;
    @Schema(description = "The price of the rent", example = "40")
    private BigDecimal price;
    @Schema(description = "The penalty of the rent", example = "35")
    private BigDecimal penalty;
    @Schema(description = "The total price which is the result of adding the penalty to the price of the rent", example = "75")
    private BigDecimal totalPrice;
    @Schema(description = "The number of rented copies", example = "2")
    private int quantity;
    
    public RentLogResponse(Rent rent) {
        this.id = rent.getId();
        this.movie = MovieListItem.from(rent.getMovie());
        this.user = new UserResponse(rent.getUser());
        this.rentDate = rent.getRentDate();
        this.expectedReturnDate = rent.getExpectedReturnDate();
        this.actualReturnDate = rent.getActualReturnDate();
        this.returned = rent.isReturned();
        this.setPrice(rent.getPrice());
        this.setPenalty(rent.getPenalty());
        this.totalPrice = rent.getTotalPrice();
        this.quantity = rent.getQuantity().intValue();
    }
}
