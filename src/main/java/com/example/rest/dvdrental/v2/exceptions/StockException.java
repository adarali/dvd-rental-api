package com.example.rest.dvdrental.v2.exceptions;

import com.example.rest.dvdrental.v2.entities.Movie;

import java.math.BigDecimal;

public class StockException extends AppException {
    public StockException(Movie movie) {
        super(getStockMessage(movie));
    }
    
    private static String getStockMessage(Movie movie) {
        if (movie.getStock().signum() < 1)
            return (String.format("The movie \"%s\" is out of stock", movie.getTitle()));
        else if (movie.getStock().compareTo(BigDecimal.ONE) > 0)
            return (String.format("There are only %d copies of the movie \"%s\" left in stock", movie.getStock().intValue(), movie.getTitle()));
        return String.format("There is only one copy of the movie \"%s\" left in stock", movie.getTitle());
    }
}
