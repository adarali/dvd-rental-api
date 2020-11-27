package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends GenericRepository<Movie, Long> {
    List<Movie> findByTitleIgnoreCaseAndIdNot(String title, Long id);
}
