package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Schema(name = "Error Response", description = "This schema is to send back to the user the error messages")
public class ErrorResponse {
    @Schema(description = "The list of error messages", example = "[\"First message\", \"Second message\"]")
    private final Set<String> messages = new HashSet<>();
    
    public ErrorResponse(Collection<String> messages) {
        this.messages.addAll(messages);
    }
    
    public ErrorResponse(String message) {
        this.messages.add(message);
    }
    
    @Schema(name = "message", description = "This is for convenience. This shows one of the messages in the list of the error messages", example = "First message")
    public String getMessage()  {
        return messages.stream().findFirst().orElse(null);
    }
}
