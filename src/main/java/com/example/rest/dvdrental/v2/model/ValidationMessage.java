package com.example.rest.dvdrental.v2.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of="field")
public class ValidationMessage {
    private String field;
    private ErrorResponse error;
    
    public ValidationMessage(String field, String message) {
        this.field = field;
        this.error = new ErrorResponse(message);
    }
    
    public ValidationMessage(String field, ErrorResponse error) {
        this.field = field;
        this.error = error;
    }
}
