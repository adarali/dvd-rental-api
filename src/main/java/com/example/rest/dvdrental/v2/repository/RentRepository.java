package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.entities.Rent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends GenericRepository<Rent, Long> {
    Rent findByUserAndMovie(AppUser user, Movie movie);
    
    @Query("select r from Rent r where (r.movie.id = :movieId or :movieId is null) and (r.user.username = :username or :username is null) and r.rentDate between :date1 and :date2")
    List<Rent> queryRents(Long movieId, String username, LocalDate date1, LocalDate date2);
}
