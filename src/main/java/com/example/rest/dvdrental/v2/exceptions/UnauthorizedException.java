package com.example.rest.dvdrental.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends AppException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}
