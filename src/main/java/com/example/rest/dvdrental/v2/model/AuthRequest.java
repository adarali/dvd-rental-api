package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name="Authentication Request", description = "Username and password for authentication")
public class AuthRequest {
    @Schema(description = "The username to authenticate with", example = "adamsandler")
    private String username;
    @Schema(description = "The password to authenticate with", example = "theearthisflat!!!")
    private String password;
    
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
