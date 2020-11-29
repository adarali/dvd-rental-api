package com.example.rest.dvdrental.v2.entities;

import com.example.rest.dvdrental.v2.exceptions.StockException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Movie extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "The title of the movie is required")
    @Size(max=100, message = "The title cannot contain more than {max} characters")
    private String title;
    @NotBlank(message = "The description of the movie is required")
    @Size(max=2000, message = "The description cannot contain more than {max} characters")
    private String description;
    @NotNull(message = "You must specify the stock")
    @Min(value=0, message = "The stock can't be below 0")
    private BigDecimal stock;
    @NotNull(message = "You must specify the rental price")
    private BigDecimal rentalPrice;
    @NotNull(message = "You must specify the sale price")
    private BigDecimal salePrice;
    private boolean available = true;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min=1, message = "The movie must have at least one image")
    @OrderBy("position")
    @JsonManagedReference
    private List<MovieImage> movieImages = new ArrayList<>();
    @JsonIgnore
    private Integer likeCount = 0;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MOVIE_LIKES",
            joinColumns = {@JoinColumn(name="movie_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    @JsonIgnore
    private List<AppUser> likes = new ArrayList<>();
    
    @Version
    @JsonIgnore
    private Long version;
    
    /**
     * Adds the specified quantity to the current stock.
     * @param quantity the specified quantity
     */
    public void addStock(BigDecimal quantity) {
        setStock(getStock().add(quantity));
    }
    
    /**
     * Subtracts the specified quantity from the current stock.
     * @param quantity the specified quantity.
     * @throws StockException if the specified quantity is greater than the current stock.
     */
    public void subtractStock(BigDecimal quantity) {
        if (quantity.compareTo(getStock()) > 0) {
            throw new StockException(this);
        }
        setStock(getStock().subtract(quantity));
    }
    
    public void updateImage(Long id, String url) {
        for (MovieImage image : movieImages) {
            if (image.getId() == id) {
                image.setUrl(url);
                break;
            }
        }
    }
    
    /**
     * Like or remove the like of a movie
     * @param user the user who likes the movie.
     * @return true if the movie was liked. false if the like was removed.
     */
    public boolean like(AppUser user) {
        if (likes.stream().anyMatch(l -> l.getId() == user.getId())) {
            likes.remove(user);
            likeCount--;
            return false;
        } else {
            likes.add(user);
            likeCount++;
            return true;
        }
    }
    
    /**
     * Check if a user liked this movie
     * @param username the username of the user
     * @return true if the user liked this movie.
     */
    public boolean isLiked(String username) {
        if(username == null) return false;
        return likes.stream().anyMatch(user -> StringUtils.equals(username, user.getUsername()));
    }
    
    public String getThumbnail() {
        MovieImage image = movieImages.stream().findFirst().orElse(null);
        if(image == null) return null;
        return image.getUrl();
    }
}
