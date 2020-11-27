package com.example.rest.dvdrental.v2.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Purchase extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Movie movie;
    @ManyToOne
    @NotNull
    private AppUser user;
    private LocalDateTime purchaseDateTime;
    @NotNull
    private BigDecimal quantity;
    
    /**
     * The price of the purchase = quantity * sale price
     * @return The price of the purchase
     */
    public BigDecimal getPrice() {
        return quantity.multiply(movie.getSalePrice());
    }
    
    public void purchase() {
        movie.subtractStock(quantity);
        this.purchaseDateTime = LocalDateTime.now();
    }
}
