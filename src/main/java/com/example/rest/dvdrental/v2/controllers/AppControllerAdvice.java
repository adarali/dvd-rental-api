package com.example.rest.dvdrental.v2.controllers;

import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class AppControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @ExceptionHandler(AppValidationException.class)
    public ResponseEntity<?> handleAppValidationException(AppValidationException ex) {
        return ResponseEntity.unprocessableEntity().body(ex.getValidationMessages());
    }
}
