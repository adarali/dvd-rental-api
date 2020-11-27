package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.AuthToken;
import com.example.rest.dvdrental.v2.exceptions.InvalidJwtTokenException;
import com.example.rest.dvdrental.v2.repository.AuthTokenRepository;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AuthTokenService extends AbstractService<AuthToken, Long> {
    
    private final AuthTokenRepository repo;
    private final JwtUtil jwtUtil;
    
    public AuthTokenService(AuthTokenRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    protected AuthTokenRepository getRepo() {
        return repo;
    }
    
    public boolean isTokenValid(String token) {
        if(!jwtUtil.isTokenValid(token)) return false;
        AuthToken authToken = getRepo().findByToken(token);
        return authToken != null && !authToken.isExpired();
    }
    
    public void validateToken(String token) {
        if (!isTokenValid(token)) {
            throw new InvalidJwtTokenException();
        }
    }
    
    public AuthToken findValidTokenByUsername(String username) {
        List<AuthToken> authTokenList = getRepo().findByUsername(username);
        return authTokenList.stream().filter(authToken -> authToken.isValid()).findFirst().orElse(null);
    }
    
    public void createToken(String token) {
        if (!isTokenValid(token)) {
            AuthToken authToken = new AuthToken();
            LocalDateTime date = jwtUtil.extractExpirationDate(token)
                    .toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            authToken.setToken(token);
            authToken.setExpirationDate(date);
            authToken.setUsername(jwtUtil.extractUsername(token));
            create(authToken);
        }
        deleteExpired();
    }
    
    @Transactional
    public void deleteToken(String token) {
        getRepo().deleteByToken(token);
        deleteExpired();
    }
    
    @Transactional
    public void deleteExpired() {
        getRepo().deleteInBatchByExpirationDateLessThan(LocalDateTime.now());
    }
    
    @Transactional
    public void deleteByUsername(String username) {
        getRepo().deleteInBatchByUsername(username);
    }
}
