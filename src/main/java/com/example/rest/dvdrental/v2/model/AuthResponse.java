package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Authentication response", description = "A model that consists of teh JWT Token for the authenticated user and the user info")
public class AuthResponse {
    @Schema(description = "The authentication token required to authenticate with the server", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2MDQyMDU3MTgsImV4cCI6MTYwNDIwOTMxOH0.jijYP89kYg0x4lHLz7BODaSQoUURNlWUnM9mE-BDiVo")
    private String jwt;
    @Schema(description = "The authenticated user's information")
    private UserResponse user;
    
    public AuthResponse(String jwt, UserResponse user) {
        this.jwt = jwt;
        this.user = user;
    }
}
