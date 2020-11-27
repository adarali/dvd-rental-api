package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRecoveryTokenRepository extends GenericRepository<PasswordRecoveryToken, Long> {
    List<PasswordRecoveryToken> findByUser(AppUser user);
    
    @Modifying
    @Query("delete from PasswordRecoveryToken prt where prt.user = :user")
    void deleteTokens(@Param("user") AppUser user);
}
