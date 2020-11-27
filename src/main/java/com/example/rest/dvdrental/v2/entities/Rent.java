package com.example.rest.dvdrental.v2.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Rent extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private AppUser user;
    private LocalDate rentDate = LocalDate.now();
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private BigDecimal quantity = BigDecimal.ONE;
    
    /**
     * The price equals quantity * rental price * days rented
     * @return
     */
    public BigDecimal getPrice() {
        return quantity.multiply(movie.getRentalPrice()).multiply(BigDecimal.valueOf(getDaysRented()));
    }
    
    /**
     * calculates the number of the delayed days
     * @return
     */
    public int getDelay() {
        //if the actual return date is before the expected return date, return 0
        LocalDate returnDate = !isReturned() ? LocalDate.now() : actualReturnDate;
        if(returnDate.isBefore(expectedReturnDate)) return 0;
        int delay = expectedReturnDate.until(returnDate).getDays();
        return delay;
    }
    
    public int getDaysRented() {
        LocalDate returnDate = !isReturned() ? LocalDate.now() : actualReturnDate;
        return rentDate.until(returnDate).getDays();
    }
    
    /**
     * The penalty is double the price for every delayed day
     * @return the penalty
     */
    public BigDecimal getPenalty() {
        return BigDecimal.valueOf(getDelay()).multiply(movie.getRentalPrice()).multiply(quantity);
    }
    
    public BigDecimal getTotalPrice() {
        return getPrice().add(getPenalty());
    }
    
    public void doRent() {
        movie.subtractStock(quantity);
    }
    
    public void returnMovie() {
        setActualReturnDate(LocalDate.now());
        movie.addStock(quantity);
    }
    
    public boolean isReturned() {
        return getActualReturnDate() != null;
    }
}
