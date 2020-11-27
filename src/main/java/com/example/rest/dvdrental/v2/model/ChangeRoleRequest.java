package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Change Role Request", description = "Change the role of the user whose username is provided")
@Getter
@Setter
public class ChangeRoleRequest {
    @Schema(description = "The username of the user to change the role of.")
    private String username;
    @Schema(description = "If this value is true, the user becomes an admin. Otherwise, he becomes/stays a mere mortal.")
    private boolean admin;
}
