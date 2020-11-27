package com.example.rest.dvdrental.v2.model.movie;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description="The url of the image of the movie", name = "Movie Image")
public class MovieImageResponse {
    @Schema(example = "4")
    private Long id;
    @Schema(example = "https://www.images.com/image002.jpg")
    private String url;
    
    public MovieImageResponse(String url) {
        this.url = url;
    }
    
    public MovieImageResponse(Long id, String url) {
        this.id = id;
        this.url = url;
    }
}
