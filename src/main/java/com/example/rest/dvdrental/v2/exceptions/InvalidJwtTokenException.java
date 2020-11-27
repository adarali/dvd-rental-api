package com.example.rest.dvdrental.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidJwtTokenException extends UnauthorizedException {
    
    public InvalidJwtTokenException() {
        super("The JWT token is invalid");
    }
    
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
