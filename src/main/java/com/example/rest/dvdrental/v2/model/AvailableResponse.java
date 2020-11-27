package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "This schema shows if the movie is available after changing its availability")
public class AvailableResponse {
    @Schema(description = "If the movie is available or not")
    boolean available;
    
    public AvailableResponse(boolean available) {
        this.available = available;
    }
}
