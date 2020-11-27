package com.example.rest.dvdrental.v2.exceptions.movie;

public class MovieUnavailableException extends MovieNotFoundException {
    
    public MovieUnavailableException() {
        super("The movie is not available");
    }
    
    public MovieUnavailableException(String message) {
        super(message);
    }
}
