package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.entities.Purchase;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import com.example.rest.dvdrental.v2.model.PurchaseLogRequest;
import com.example.rest.dvdrental.v2.model.PurchaseLogResponse;
import com.example.rest.dvdrental.v2.model.PurchaseRequest;
import com.example.rest.dvdrental.v2.repository.PurchaseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService extends AbstractService<Purchase, Long> {
    
    private final PurchaseRepository repo;
    private final MovieService movieService;
    private final AppUserService userService;
    
    public PurchaseService(PurchaseRepository repo, MovieService movieService, AppUserService userService) {
        this.repo = repo;
        this.movieService = movieService;
        this.userService = userService;
    }
    
    @Override
    protected PurchaseRepository getRepo() {
        return repo;
    }
    
    @Transactional
    public void purchase(PurchaseRequest request, String username) {
        Purchase purchase = new Purchase();
        Movie movie = movieService.getMovie(request.getMovieId());
        if(!movie.isAvailable()) {
            throw new AppException("The movie is not available");
        }
        purchase.setMovie(movie);
        purchase.setUser(userService.findByUsername(username));
        purchase.setQuantity(BigDecimal.valueOf(request.getQuantity()));
        purchase.purchase();
        create(purchase);
    }
    
    public List<PurchaseLogResponse> getLogs(PurchaseLogRequest request) {
        if (request.getDateFrom() == null || request.getDateTo() == null) {
            throw new AppValidationException("You have to provide both the initial and the final dates");
        }
        List<Purchase> purchases = repo.queryPurchases(request.getMovieId(), StringUtils.trimToNull(request.getUsername()), request.getDateFrom().atStartOfDay(), request.getDateTo().plusDays(1).atStartOfDay());
        return purchases.stream()
                .filter(r -> StringUtils.trimToNull(request.getMovieTitle()) == null || r.getMovie().getTitle().toLowerCase().contains(request.getMovieTitle().toLowerCase()))
                .filter(r -> StringUtils.trimToNull(request.getUserFullName()) == null || r.getUser().getFullName().toLowerCase().contains(request.getUserFullName().toLowerCase()))
                .map(PurchaseLogResponse::new).collect(Collectors.toList());
    }
    
}
