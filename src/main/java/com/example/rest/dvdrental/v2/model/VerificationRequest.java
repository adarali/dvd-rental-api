package com.example.rest.dvdrental.v2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    private String username;
    private String password;
    private String code;
}
