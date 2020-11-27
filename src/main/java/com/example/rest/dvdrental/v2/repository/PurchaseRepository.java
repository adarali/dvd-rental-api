package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.Purchase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends GenericRepository<Purchase, Long> {
    @Query("select r from Purchase r where (r.movie.id = :movieId or :movieId is null) and (r.user.username = :username or :username is null) and r.purchaseDateTime between :date1 and :date2")
    List<Purchase> queryPurchases(Long movieId, String username, LocalDateTime date1, LocalDateTime date2);

}
