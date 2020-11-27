package com.example.rest.dvdrental.v2.exceptions;

import com.example.rest.dvdrental.v2.model.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class AppValidationException extends AppException {
    
    private final Map<String, ValidationMessage> validationMessageMap = new HashMap<>();
    
    public AppValidationException() {
        super("Validation error");
    }
    
    public AppValidationException(String message) {
        super(message);
    }
    
    public AppValidationException(Collection<ValidationMessage> validationMessages) {
        super(validationMessages.stream().map(it -> it.getError().getMessage()).collect(Collectors.toList()));
        validationMessages.forEach(msg -> addValidationMessage(msg));
    }
    
    public Collection<ValidationMessage> getValidationMessages() {
        return validationMessageMap.values();
    }
    
    
    
    public void addValidationMessage(ValidationMessage validationMessage) {
        if (validationMessageMap.containsKey(validationMessage.getField())) {
            ValidationMessage vm = validationMessageMap.get(validationMessage.getField());
            vm.getError().getMessages().addAll(validationMessage.getError().getMessages());
        } else {
            validationMessageMap.put(validationMessage.getField(), validationMessage);
        }
    }
    
    public boolean hasMessages() {
        return !this.validationMessageMap.isEmpty();
    }
    
    public void throwMe() {
        if (hasMessages()) {
            throw this;
        }
    }
}
