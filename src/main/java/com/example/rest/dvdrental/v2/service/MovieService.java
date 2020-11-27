package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.exceptions.ResourceExistsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.ChangeLog;
import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.entities.MovieImage;
import com.example.rest.dvdrental.v2.enums.ChangeType;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.exceptions.movie.MovieNotFoundException;
import com.example.rest.dvdrental.v2.exceptions.movie.MovieUnavailableException;
import com.example.rest.dvdrental.v2.model.LazyRequest;
import com.example.rest.dvdrental.v2.model.LazyResponse;
import com.example.rest.dvdrental.v2.model.LikeResponse;
import com.example.rest.dvdrental.v2.model.ValidationMessage;
import com.example.rest.dvdrental.v2.model.movie.MovieDetails;
import com.example.rest.dvdrental.v2.model.movie.MovieListItem;
import com.example.rest.dvdrental.v2.repository.MovieRepository;
import com.example.rest.dvdrental.v2.utils.AppUtils;
import lombok.extern.java.Log;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log
public class MovieService extends AbstractService<Movie, Long> {
    
    @PersistenceContext
    private EntityManager em;
    
    private final MovieRepository repo;
    private final Validator validator;
    private final ObjectMapper objectMapper;
    private final AppUserService appUserService;
    private final ChangeLogService changeLogService;
    
    public MovieService(MovieRepository repo, Validator validator, ObjectMapper objectMapper, AppUserService appUserService, ChangeLogService changeLogService) {
        this.repo = repo;
        this.validator = validator;
        this.objectMapper = objectMapper;
        this.appUserService = appUserService;
        this.changeLogService = changeLogService;
    }
    
    @Override
    protected MovieRepository getRepo() {
        return repo;
    }
    
    public void removeMovie(Long id) {
        if (id == null) {
            throw new AppException("The id was not specified");
        }
        Movie movie = find(id);
        if (movie == null) {
            throw new AppException(String.format("The movie with id %d doesn't exist", id));
        }
        super.deleteById(id);
    }
    
    @Transactional
    public Movie toggleAvailability(Long id) {
        Movie movie = getMovie(id);
        movie.setAvailable(!movie.isAvailable());
        return movie;
    }
    
    @Override
    public Movie create(Movie entity) {
        entity.setId(null);
        entity.getMovieImages().forEach(img -> img.setMovie(entity));
        return super.create(entity);
    }
    
    @Transactional
    public Movie update(Map<String, Object> map) {
        if (map.get("id") == null) {
            throw new AppException("You must specify the id of the movie you want to modify");
        }
        Long id = ((Integer) map.get("id")).longValue();
        try {
            Movie movie = getMovie(id);
            createChangeLog(map, movie);
            /**
             * Checking if the map contains the key "movieImages" which is a json property.
             * If the map contains said key, it extracts the image info and convert it to a valid list of type MovieImage.
             * Then this code compares the newly created image list to the one existing in the database
             * If the image in the list from the database doesn't exist in the newly created list, it is removed from the database.
             * The images that exist in the newly created list but not in the list from the database are added to the database.
             */
            if (map.containsKey("movieImages")) {
                List<MovieImage> movieImages = new ArrayList<>();
                int i = 0;
                for (Object obj : ((List) map.get("movieImages"))) {
                    Map<String, Object> imageMap = ((Map<String, Object>) obj);
                    if(imageMap.get("url") == null) continue;
                    MovieImage image = new MovieImage(movie, imageMap.get("url").toString());
                    image.setPosition(i++);
                    movieImages.add(image);
                }
                movie.getMovieImages().clear();
                for (MovieImage mi : movieImages) {
                    if (movie.getMovieImages().stream().noneMatch(img -> StringUtils.equals(mi.getUrl(), img.getUrl()))) {
                        movie.getMovieImages().add(mi);
                    }
                }
                map.remove("movieImages");
            }
            em.detach(movie);
            objectMapper.readerForUpdating(movie).readValue(objectMapper.writeValueAsString(map));
            validate(movie);
            return super.save(movie);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Movie save(Movie entity) {
        validate(entity);
        return super.save(entity);
    }
    
    @Override
    protected void validate(Movie entity, List<ValidationMessage> validationMessageList) {
        
        for (MovieImage image : entity.getMovieImages()) {
            val imageViolations = validator.validate(image);
            for (val violation : imageViolations) {
                validationMessageList.add(new ValidationMessage("movieImages", violation.getMessage()));
            }
        }
        
        List<Movie> movies = repo.findByTitleIgnoreCaseAndIdNot(StringUtils.trimToNull(entity.getTitle()), entity.getId() == null? -1 : entity.getId());
        
        if (movies.stream().anyMatch(movie -> movie.getTitle().equalsIgnoreCase(entity.getTitle()))) {
//            validationMessageList.add(new ValidationMessage("title", "Another movie with the same title exists in the database"));
            throw new ResourceExistsException("Another movie with the same title exists in the database");
        }
    
        if (entity.getRentalPrice().compareTo(new BigDecimal("0.01")) < 0) {
            validationMessageList.add(new ValidationMessage("rentalPrice", "The rental price must be at least 0.01"));
        }
        
        if (entity.getSalePrice().compareTo(new BigDecimal("0.01")) < 0) {
            validationMessageList.add(new ValidationMessage("salePrice", "The sale price must be at least 0.01"));
        }
        
        super.validate(entity, validationMessageList);
    }
    
    public LazyResponse<Movie> findAll(LazyRequest request) {
        Specification<Movie> specification = (Specification<Movie>) (root, query, cb) ->  {
            List<Predicate> searchPredicates = new ArrayList<>();
            request.getFilters().forEach((k, v) -> {
                switch(k) {
                    case "title" :  {
                        searchPredicates.add(cb.like(cb.lower(root.get(k)), "%"+StringUtils.trimToEmpty(v instanceof String ? v.toString() : null).toLowerCase()+"%"));
                    }
                    case "available" : {
                        if (v instanceof Integer) {
                            switch((Integer) v) {
                                case 0: case 1: searchPredicates.add(cb.equal(root.get(k), v.equals(1)));
                            }
                        }
                    }
                    
                }
            });
            return cb.and(searchPredicates.toArray(new Predicate[]{}));
        };
        if (!Arrays.asList("likeCount", "likes", "title").contains(request.getSortField())) {
            request.setSortField("title");
        }
        Page<Movie> page = getRepo().findAll(specification, request.getPageRequest());
        return new LazyResponse<>(page.getContent(), page.getTotalElements());
    }
    
    @Transactional(readOnly = true)
    public LazyResponse<MovieListItem> getMovieListItems(LazyRequest request) {
        if (StringUtils.isBlank(request.getSortField())) {
            request.setSortField("title");
        }
        LazyResponse<Movie> response = findAll(request);
        return new LazyResponse<>(response.getData().stream().map(movie -> MovieListItem.from(movie).thumbnail(movie.getThumbnail()))
                .collect(Collectors.toList()), response.getTotalRecords());
    }
    
    @Transactional
    public LikeResponse likeMovie(Long id, String username) {
        Movie movie = getAvailableMovie(id);
        boolean liked = movie.like(appUserService.findByUsername(username));
        return new LikeResponse(liked, movie.getLikeCount());
    }
    
    private void createChangeLog(Map<String, Object> map, Movie movie) throws JsonProcessingException {
        Movie newMovie = new Movie();
        objectMapper.readerForUpdating(newMovie).readValue(objectMapper.writeValueAsString(map));
    
        List<ChangeLog> changeLogs = new ArrayList<>();
        if (newMovie.getTitle() != null && !newMovie.getTitle().equals(movie.getTitle())) {
            changeLogs.add(new ChangeLog(movie.getTitle(), newMovie.getTitle(), ChangeType.TITLE, movie));
        }
        if (newMovie.getSalePrice() != null && newMovie.getSalePrice().compareTo(movie.getSalePrice()) != 0) {
            changeLogs.add(new ChangeLog(movie.getSalePrice().toString(), newMovie.getSalePrice().toString(), ChangeType.SALE_PRICE, movie));
        }
        if (newMovie.getRentalPrice() != null && newMovie.getRentalPrice().compareTo(movie.getRentalPrice()) != 0) {
            changeLogs.add(new ChangeLog(movie.getRentalPrice().toString(), newMovie.getRentalPrice().toString(), ChangeType.RENTAL_PRICE, movie));
        }
        for (ChangeLog changeLog : changeLogs) {
            changeLogService.create(changeLog);
        }
    }
    
    public Movie getMovie(Long id) {
        Movie movie = find(id);
        if (movie == null) {
            throw new MovieNotFoundException();
        }
        return movie;
    }
    
    public Movie getAvailableMovie(Long id) {
        Movie movie = getMovie(id);
        if (!movie.isAvailable()) {
            throw new MovieUnavailableException();
        }
        return movie;
    }
    
    @Transactional(readOnly = true)
    public MovieDetails getMovieDetails(Long id, String username) {
        AppUser user = appUserService.findByUsernameOrNull(username);
        Movie movie = user == null || !user.isAdmin() ? getAvailableMovie(id) : getMovie(id);
        MovieDetails details = MovieDetails.from(movie);
        details.setLiked(movie.isLiked(username));
        return details;
    }
    
    @Override
    public void delete(Movie entity) {
        try {
            super.delete(entity);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new AppException("Cannot delete a movie which already has been purchased or rented");
        }
    }
}
