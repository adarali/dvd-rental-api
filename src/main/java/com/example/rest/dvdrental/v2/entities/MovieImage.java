package com.example.rest.dvdrental.v2.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class MovieImage extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @ManyToOne
    @JsonBackReference
    private Movie movie;
    @NotBlank(message = "The image URL is required")
    @Size(max=1000, message = "The image URL is too long. It must be no more than {max} characters.")
    @URL(message = "The image URL is not valid")
    @Schema(description = "The URL of the image")
    private String url;
    @JsonIgnore
    private int position;
    
    
    public MovieImage(Movie movie, String url) {
        this.movie = movie;
        this.url = url;
    }
    
    
}
