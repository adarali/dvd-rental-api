package com.example.rest.dvdrental.v2.exceptions;

public class InvalidCredentialsException extends UnauthorizedException {
    
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
