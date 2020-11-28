package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.entities.Rent;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import com.example.rest.dvdrental.v2.exceptions.ResourceNotFoundException;
import com.example.rest.dvdrental.v2.model.RentLogRequest;
import com.example.rest.dvdrental.v2.model.RentLogResponse;
import com.example.rest.dvdrental.v2.model.RentRequest;
import com.example.rest.dvdrental.v2.model.ValidationMessage;
import com.example.rest.dvdrental.v2.repository.RentRepository;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RentService extends AbstractService<Rent, Long> {
    
    private final RentRepository repo;
    private final MovieService movieService;
    private final AppUserService appUserService;
    
    public RentService(RentRepository repo, MovieService movieService, AppUserService appUserService) {
        this.repo = repo;
        this.movieService = movieService;
        this.appUserService = appUserService;
    }
    
    @Override
    protected RentRepository getRepo() {
        return repo;
    }
    
    @Transactional
    public Rent rent(RentRequest rentRequest, String username) {
        val violations = validator.validate(rentRequest);
        Set<ValidationMessage> validationMessages = new HashSet<>();
        for (val violation : violations) {
            validationMessages.add(new ValidationMessage(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        new AppValidationException(validationMessages).throwMe();
        Movie movie = movieService.getAvailableMovie(rentRequest.getMovieId());
        AppUser user = appUserService.findByUsername(username);
        Rent dbRent = findByUserAndMovie(user, movie);
        if (dbRent != null && !dbRent.isReturned()) {
            throw new AppException(String.format("You have not returned the movie \"%s\"", movie.getTitle()));
        }
        Rent rent = new Rent();
        rent.setMovie(movie);
        rent.setUser(user);
        rent.setQuantity(BigDecimal.valueOf(rentRequest.getQuantity()));
        rent.setExpectedReturnDate(rent.getRentDate().plusDays(rentRequest.getRentDays()));
        rent.doRent();
        return create(rent);
    }
    
    private Rent findByUserAndMovie(AppUser user, Movie movie) {
        return repo.findByUserAndMovie(user, movie);
    }
    
    public Rent getRent(Long id) {
        Rent rent = find(id);
        if (rent == null) {
            throw new ResourceNotFoundException("The rent does not exist");
        }
        return rent;
    }
    
    @Transactional
    public RentLogResponse returnRent(Long id) {
        Rent rent = getRent(id);
        rent.returnMovie();
        return new RentLogResponse(rent);
    }
    
    public List<RentLogResponse> getRentLogs(RentLogRequest request) {
        if (request.getDateFrom() == null || request.getDateTo() == null) {
            throw new AppValidationException("You have to provide both the initial and the final dates");
        }
        List<Rent> rents = repo.queryRents(request.getMovieId(), StringUtils.trimToNull(request.getUsername()), request.getDateFrom(), request.getDateTo());
        return rents.stream().filter(r -> request.getReturned() == null || r.isReturned() == request.getReturned())
                .filter(r -> StringUtils.trimToNull(request.getMovieTitle()) == null || r.getMovie().getTitle().toLowerCase().contains(request.getMovieTitle().toLowerCase()))
                .filter(r -> StringUtils.trimToNull(request.getUserFullName()) == null || r.getUser().getFullName().toLowerCase().contains(request.getUserFullName().toLowerCase()))
                .map(RentLogResponse::new).collect(Collectors.toList());
    }
    
    
    
}
