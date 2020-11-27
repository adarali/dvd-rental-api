package com.example.rest.dvdrental.v2.exceptions.user;

import com.example.rest.dvdrental.v2.exceptions.ResourceNotFoundException;

public class AppUserNotFoundException extends ResourceNotFoundException {
    
    public AppUserNotFoundException() {
        super("The user was not found");
    }
    
    public AppUserNotFoundException(String message) {
        super(message);
    }
}
