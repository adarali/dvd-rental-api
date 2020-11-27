package com.example.rest.dvdrental.v2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean admin;
}
