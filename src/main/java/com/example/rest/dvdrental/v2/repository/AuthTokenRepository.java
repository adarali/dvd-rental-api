package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.AuthToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuthTokenRepository extends GenericRepository<AuthToken, Long> {
    AuthToken findByToken(String token);
    
    @Modifying
    void deleteByToken(String token);
    
    @Modifying
    void deleteInBatchByExpirationDateLessThan(LocalDateTime date);
    
    @Modifying
    void deleteInBatchByUsername(String username);
    
    List<AuthToken> findByUsername(String username);
}
