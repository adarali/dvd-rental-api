package com.example.rest.dvdrental.v2.model.movie;

import com.example.rest.dvdrental.v2.entities.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MovieListItem {
    @Schema(example = "5", description = "The ID of the movie")
    private Long id;
    @Schema(example = "A walk to remember", description = "The title of the movie")
    private String title;
    @Schema(example = "69", description = "The number of likes the movie has received")
    private int likes;
    @Schema(example = "true", description = "If the movie is available")
    private boolean available;
    @Schema(example = "20" ,description = "The rental price of the movie")
    private BigDecimal rentalPrice;
    @Schema(example = "50", description = "The sale price of the movie")
    private BigDecimal salePrice;
    @Schema(example = "http://www.google.com/thumbnail001.jpg", description = "The thumbnail of the movie")
    private String thumbnail;
    
    
    public static MovieListItem from(Movie movie) {
        MovieListItem item = new MovieListItem();
        item.setId(movie.getId());
        item.setTitle(movie.getTitle());
        item.setLikes(movie.getLikeCount());
        item.setAvailable(movie.isAvailable());
        item.setRentalPrice(movie.getRentalPrice());
        item.setSalePrice(movie.getSalePrice());
        return item;
    }
    
    public MovieListItem thumbnail(String url) {
        this.thumbnail = url;
        return this;
    }
}
