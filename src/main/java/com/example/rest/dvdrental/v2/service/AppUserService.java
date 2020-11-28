package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.PasswordRecoveryToken;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import com.example.rest.dvdrental.v2.exceptions.ResourceExistsException;
import com.example.rest.dvdrental.v2.exceptions.user.AppUserNotFoundException;
import com.example.rest.dvdrental.v2.model.UserRequest;
import com.example.rest.dvdrental.v2.repository.AppUserRepository;
import com.example.rest.dvdrental.v2.utils.AppUtils;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Log
public class AppUserService extends AbstractService<AppUser, Long> {
    
    private final AppUserRepository repo;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    
    public AppUserService(AppUserRepository repo, PasswordRecoveryTokenService passwordRecoveryTokenService, EmailService emailService, PasswordEncoder passwordEncoder, ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.repo = repo;
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    protected AppUserRepository getRepo() {
        return repo;
    }
    
    /**
     * Finds the user in the database with the specified username
     * @param username The specified username
     * @return The user with the specified username
     * @throws AppUserNotFoundException if there are not matches in the database
     */
    public AppUser findByUsername(String username) {
        AppUser user = findByUsernameOrNull(username);
        if (user == null) {
            throw new AppUserNotFoundException();
        }
        return user;
    }
    
    /**
     * Convenience method that returns null if the user is not found instead of throwing an exception
     * @param username the username of the user
     * @return the user if found or null.
     */
    public AppUser findByUsernameOrNull(String username) {
        return findByUsernameOptional(username).orElse(null);
    }
    
    public Optional<AppUser> findByUsernameOptional(String username) {
        return getRepo().findByUsername(username);
    }
    
    /**
     * Sets the state of the user as verified
     * @param username The username of the user
     */
    @Transactional
    public void setUserVerified(String username) {
        AppUser user = findByUsername(username);
        if (user != null) {
            user.setVerified(true);
        }
    }
    
    @Transactional
    public void changeRole(String username, boolean admin) {
        findByUsername(username).setAdmin(admin);
    }
    
    /**
     * Sends a recovery email to the user
     * @param user the user to send the email to.
     * @throws AppException if the email has not been verified
     */
    public void sendRecoveryEmail(AppUser user) {
        if (!user.isVerified()) {
            throw new AppException(String.format("The email has not been verified for the user %s", user.getUsername()));
        }
        String token = jwtUtil.generateToken(user, 120, jwtUtil.generateSecretKey(user.getPassword()));
//        PasswordRecoveryToken token = passwordRecoveryTokenService.createToken(user);
        emailService.sendRecoveryEmail(user, token);
    }
    
    public void sendRecoveryEmail(String username) {
        sendRecoveryEmail(findByUsername(username));
    }
    
    /**
     * Changes the user's password
     * @param user The user of which the password is changed
     * @param password The new password
     */
    public void changePassword(AppUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        update(user);
    }
    
    public AppUser saveUser(UserRequest request) {
        AppUser user = repo.findByUsername(request.getUsername()).orElse(new AppUser());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAdmin(request.isAdmin());
        return createOrUpdate(user);
    }
    
    public AppUser saveUser(Map<String, Object> map, boolean creating) {
        if(map.get("username") == null) throw new AppValidationException("The username is required");
        String username = map.get("username").toString();
        AppUser user = repo.findByUsername(username).orElse(new AppUser());
        if(creating && user.getId() != null)
            throw new ResourceExistsException("Another user with the same username already exists in the database");
        if (map.get("password") != null) {
            user.setPassword(passwordEncoder.encode(map.get("password").toString()));
            map.remove("password");
        }
        try {
            objectMapper.readerForUpdating(user).readValue(objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return createOrUpdate(user);
    }
    
    public AppUser saveUser(DefaultOAuth2User oAuth2User, String registrationId) {
        final String username = String.format("%s_%s", registrationId, oAuth2User.getName());
        Optional<AppUser> optional = findByUsernameOptional(username);
        if(optional.isPresent()) return optional.get();
        AppUser user = new AppUser(username);
        user.setPassword(passwordEncoder.encode(AppUtils.generatePassword(32)));
        user.setSource(registrationId);
        if ("google".equals(registrationId)) {
            user.setFirstName(String.valueOf(oAuth2User.getAttributes().get("given_name")));
            Object lastName = oAuth2User.getAttributes().get("family_name");
            if (lastName != null) {
                user.setLastName(String.valueOf(lastName));
            }
            user.setEmail(String.valueOf(oAuth2User.getAttributes().get("email")));
            String picture = (String) oAuth2User.getAttributes().get("picture");
            if (picture != null) {
                user.setPicture(picture);
            }
        } else if ("github".equals(registrationId)) {
            if (oAuth2User.getAttributes().get("email") != null) {
                user.setEmail(String.valueOf(oAuth2User.getAttributes().get("email")));
            }
            user.setFullName(String.valueOf(oAuth2User.getAttributes().get("name")));
            String picture = (String) oAuth2User.getAttributes().get("avatar_url");
            if (picture != null) {
                user.setPicture(picture);
            }
            
        } else {
            throw new RuntimeException("Unsupported OAuth provider");
        }
        
        
        
        return create(user);
    }
    
    public void deleteByUsername(String username) {
        delete(findByUsername(username));
    }
    
    public long countAdmin() {
        return getRepo().countAdmin();
    }
    
    
}
