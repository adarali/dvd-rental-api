package com.example.rest.dvdrental.v2.model;

import com.example.rest.dvdrental.v2.entities.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "User Response")
public class UserResponse {
    @Schema(description = "The username of the user", example = "adamsandler")
    private String username;
    @Schema(description = "The first name of the user", example = "Adam")
    private String firstName;
    @Schema(description = "The last name of the user", example = "Sandler")
    private String lastName;
    @Schema(description = "The full name of the user", example = "Adam Sandler")
    private String fullName;
    @Schema(description = "The email of the user", example = "adamsandler@gmail.com")
    private String email;
    @Schema(description = "If the user has admin privileges. true if yes, false if not", example = "false")
    private boolean admin;
    @Schema(description = "If the user is enabled so he can access resources that require authentication. true if yes, false if no", example = "true")
    private boolean enabled;
    @Schema(description = "If the user's email is verified. true if yes, false if no", example = "true")
    private boolean verified;
    @Schema(description = "The URL of the user's profile picture", example = "http://www.images.com/image002.jpg")
    private String picture;
    
    public UserResponse(AppUser user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.admin = user.isAdmin();
        this.enabled = user.isEnabled();
        this.verified = user.isVerified();
        this.picture = user.getPicture();
    }
}
