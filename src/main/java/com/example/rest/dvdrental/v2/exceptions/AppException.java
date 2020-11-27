package com.example.rest.dvdrental.v2.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppException extends RuntimeException {
    
    @Getter
    private final List<String> messages = new ArrayList<>();
    
    public AppException(String message) {
        super(message);
        this.messages.add(message);
    }
    
    public AppException(Collection<String> messages) {
        super(messages.stream().findFirst().orElse(null));
        this.messages.addAll(messages);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }
}
