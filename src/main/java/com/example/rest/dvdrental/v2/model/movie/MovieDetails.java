package com.example.rest.dvdrental.v2.model.movie;

import com.example.rest.dvdrental.v2.entities.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "The details of the movie", name = "Movie Details")
public class MovieDetails {
    @Schema(description = "The ID of the movie", example = "1")
    private Long id;
    @Schema(description = "The title of the movie", example = "Desperado")
    private String title;
    @Schema(description = "The description of the movie", example = "Some description")
    private String description;
    @Schema(description = "The rental price of the movie", example = "20")
    private BigDecimal rentalPrice;
    @Schema(description = "The sale price of the movie", example = "50")
    private BigDecimal salePrice;
    @Schema(description = "The quantity in stock", example = "35")
    private BigDecimal stock;
    @Schema(description = "The number of likes the movie has received")
    private int likes;
    @Schema(description = "The availability of the movie", example = "true")
    private boolean available;
    @Schema(description = "True if the authenticated user has liked this movie, false is not. If the user is not authenticated, the value is false.", example = "false")
    private boolean isLiked;
    private List<MovieImageResponse> movieImages = new ArrayList<>();
    
    public static MovieDetails from(Movie movie) {
        MovieDetails details = new MovieDetails();
        details.setId(movie.getId());
        details.setTitle(movie.getTitle());
        details.setDescription(movie.getDescription());
        details.setRentalPrice(movie.getRentalPrice());
        details.setSalePrice(movie.getSalePrice());
        details.setStock(movie.getStock());
        details.setLikes(movie.getLikeCount());
        details.setAvailable(movie.isAvailable());
        movie.getMovieImages().forEach(img -> details.getMovieImages().add(new MovieImageResponse(img.getId(), img.getUrl())));
        return details;
    }
}
