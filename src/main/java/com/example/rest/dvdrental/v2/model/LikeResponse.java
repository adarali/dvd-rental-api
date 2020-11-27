package com.example.rest.dvdrental.v2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "This schema shows if the movie is liked and the number of likes the liked movie has received")
public class LikeResponse {
    @Schema(description = "If the movie is liked or not")
    private boolean liked;
    @Schema(description = "The number of likes the movie has received", example = "1")
    private int likes;
    
    public LikeResponse(boolean liked, int likes) {
        this.liked = liked;
        this.likes = likes;
    }
}
