package com.example.rest.dvdrental.v2.exceptions;

public class InvalidJwtTokenException extends UnauthorizedException {
    
    public InvalidJwtTokenException() {
        super("The JWT token is invalid");
    }
    
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
