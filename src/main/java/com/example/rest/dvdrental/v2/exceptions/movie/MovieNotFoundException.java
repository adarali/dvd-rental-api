package com.example.rest.dvdrental.v2.exceptions.movie;

import com.example.rest.dvdrental.v2.exceptions.ResourceNotFoundException;

public class MovieNotFoundException extends ResourceNotFoundException {
    
    public MovieNotFoundException() {
        super("The movie does not exist in the database");
    }
    
    public MovieNotFoundException(String message) {
        super(message);
    }
}

