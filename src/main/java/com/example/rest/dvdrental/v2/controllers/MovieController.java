package com.example.rest.dvdrental.v2.controllers;

import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.model.*;
import com.example.rest.dvdrental.v2.model.movie.MovieDetails;
import com.example.rest.dvdrental.v2.model.movie.MovieListItem;
import com.example.rest.dvdrental.v2.model.movie.MovieQuery;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.service.MovieService;
import com.example.rest.dvdrental.v2.service.PurchaseService;
import com.example.rest.dvdrental.v2.service.RentService;
import com.example.rest.dvdrental.v2.utils.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("api/v1/movies")
@Log
public class MovieController {
    
    private final MovieService movieService;
    private final AppUserService appUserService;
    private final RentService rentService;
    private final PurchaseService purchaseService;
    
    public MovieController(MovieService movieService, AppUserService appUserService, RentService rentService, PurchaseService purchaseService) {
        this.movieService = movieService;
        this.appUserService = appUserService;
        this.rentService = rentService;
        this.purchaseService = purchaseService;
    }
    
    @Operation(summary = "Get list of movies", description = "Gets a list of movies based on the parameters passed by the user")
    @ResponseStatus
    @GetMapping
    public ResponseEntity<?> getMovies(
            @Validated @Parameter(name = "Query parameters", content = @Content(schema = @Schema(implementation = MovieQuery.class))) MovieQuery query,
            @RequestParam(value="per_page", required = false, defaultValue = "10") @Parameter(description = "The number of records to show per page") int pageSize,
            HttpServletRequest req,
            Principal principal) throws URISyntaxException {
        boolean admin = false;
        if (principal instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authenticationToken = ((JwtAuthenticationToken) principal);
            admin = authenticationToken.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        
        //Always show available movies only if the user is not an admin.
        if (!admin) {
            query.setAvailable(1);
        }
        query.setPageSize(pageSize);
        LazyRequest request = query.toLazyRequest();
        LazyResponse<MovieListItem> response = movieService.getMovieListItems(request);
        String linkHeader = AppUtils.buildPaginationLinkHeaders(request, response, req);
        
        return ResponseEntity.ok().header("Link", linkHeader).header("X-Total-Count", String.valueOf(response.getTotalRecords())).body(response.getData());
    }
    
    @Operation(summary = "Get the movie details by id", description = "This operation returns an object with the movie details")
    @ApiResponse(description = "The movie details", content = @Content(schema = @Schema(implementation = MovieDetails.class)), responseCode = "200")
    @ApiResponse(responseCode = "404", description = "The movie id is not found in the database")
    @GetMapping("{id}")
    public MovieDetails getDetails(@Parameter(description = "The ID of the movie.") @PathVariable("id") Long id, Principal principal) {
        log.info("Getting details of movie ID: " + id);
        String username = principal != null ? principal.getName() : null;
        MovieDetails movie = movieService.getMovieDetails(id, username);
        return movie;
    }
    @Operation(summary = "Adds a new movie (Requires admin privileges)", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"title\":\"Desperado\",\"description\":\"Some description\",\"stock\":35,\"rentalPrice\":20,\"salePrice\":50,\"available\":true,\"movieImages\":[{\"url\":\"https://www.images.com/image002.jpg\"}]}"))))
    @ApiResponse(description = "Returns the created movie details", content = @Content(schema = @Schema(implementation = MovieDetails.class)), responseCode = "200")
    @ApiResponse(responseCode = "422", description = "The data provided contain errors. The response is a list of fields with the error messages corresponding to each field", content = @Content(schema = @Schema(example = "{\"messages\":[{\"field\":\"rentalPrice\",\"error\":{\"messages\":[\"You must specify the rental price\"],\"message\":\"You must specify the rental price\"}},{\"field\":\"title\",\"error\":{\"messages\":[\"The title of the movie is required\"],\"message\":\"The title of the movie is required\"}}]}")))
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> create(@RequestBody Movie movie) {
        MovieDetails details = MovieDetails.from(movieService.create(movie));
        Link link = linkTo(methodOn(getClass()).getDetails(details.getId(), null)).withSelfRel();
        return ResponseEntity.created(link.toUri()).body(details);
    }
    
    @Operation(summary= "Update a movie by ID", description="This operation updates a movie in the database")
    @ApiResponse(responseCode = "204", description = "If the update was successful")
    @PutMapping("{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MovieDetails> update(@Parameter(description = "The ID of the movie.") @PathVariable("id") Long id, @RequestBody Map<String, Object> movie) {
        movie.put("id", id);
        MovieDetails details = MovieDetails.from(movieService.update(movie));
        return ResponseEntity.ok().body(details);
    }
    
    @Operation(summary = "Delete a movie by ID", description="This operation deletes a movie from the database", parameters = @Parameter(in = ParameterIn.PATH, name="id", description = "The ID of the movie to be deleted"))
    @ApiResponse(responseCode = "204", description = "If the deletion is successful")
    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        movieService.getMovie(id);
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(description = "This operation toggles the like of a movie", summary = "Like / Unlike a movie")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LikeResponse.class)))
    @PatchMapping("{id}/like")
    public ResponseEntity<?> likeMovie(@PathVariable @Parameter(description = "The ID of the movie") Long id, Principal principal) {
        LikeResponse response = movieService.likeMovie(id, principal.getName());
        return ResponseEntity.ok(response);
    }
    
    @Operation(description = "This operation toggles the availability of a movie")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AvailableResponse.class)))
    @PatchMapping("{id}/available")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> makeAvailable(@PathVariable Long id, Principal principal) {
        val resp = movieService.toggleAvailability(id);
        return ResponseEntity.ok(new AvailableResponse(resp.isAvailable()));
    }
    
    @Operation(summary = "Rent a movie (Requires authentication)")
    @ApiResponse(responseCode = "200")
    @PostMapping("{id}/rents")
    public ResponseEntity<?> rent(@RequestBody RentRequest rentRequest, Principal principal) {
        rentService.rent(rentRequest, principal.getName());
        return ResponseEntity.ok("Action completed successfully");
    }
    
    @Operation(summary = "Purchase a movie (Requires authentication)", description = "This operation is to purchase a movie by providing the ID of the movie and the quantity to purchase")
    @ApiResponse(responseCode = "200")
    @PostMapping("{id}/purchases")
    public ResponseEntity<?> purchase(@RequestBody PurchaseRequest request, Principal principal) {
        purchaseService.purchase(request, principal.getName());
        return ResponseEntity.ok("Purchase completed successfully");
    }
    
    @Operation(summary = "Return a rented movie (Requires admin privileges)", parameters = @Parameter(in = ParameterIn.QUERY, name = "id", description = "The id of the rent of the movie"))
    @ApiResponse(responseCode = "200")
    @PatchMapping("{id}/rents/{rentId}/return")
    public RentLogResponse returnRent(@PathVariable("rentId") Long rentId) {
        return rentService.returnRent(rentId);
    }
}
