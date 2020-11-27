package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.PasswordRecoveryToken;
import com.example.rest.dvdrental.v2.repository.PasswordRecoveryTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PasswordRecoveryTokenService extends AbstractService<PasswordRecoveryToken, Long> {
    
    private final PasswordRecoveryTokenRepository repo;
    
    public PasswordRecoveryTokenService(PasswordRecoveryTokenRepository repo) {
        this.repo = repo;
    }
    
    @Override
    protected PasswordRecoveryTokenRepository getRepo() {
        return repo;
    }
    
    /**
     * Creates a password recovery token for the specified user
     * @param user The specified user
     * @return The created token
     */
    @Transactional
    public PasswordRecoveryToken createToken(AppUser user) {
        repo.deleteTokens(user);
        PasswordRecoveryToken token = new PasswordRecoveryToken(user);
        return create(token);
    }
    
    public boolean validateToken(String token, AppUser user) {
        List<PasswordRecoveryToken> tokenList = repo.findByUser(user);
        return tokenList.stream().anyMatch(t -> t.getToken().equals(token) && t.isValid());
    }
}
