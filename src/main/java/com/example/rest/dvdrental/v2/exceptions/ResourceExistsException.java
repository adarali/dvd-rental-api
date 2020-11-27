package com.example.rest.dvdrental.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends AppException {
    
    public ResourceExistsException() {
        this("Resource already exists");
    }
    
    public ResourceExistsException(String message) {
        super(message);
    }
}
