package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends GenericRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    
    @Query("select count(*) from AppUser user where user.role = 'ADMIN'")
    long countAdmin();
    
    
}
