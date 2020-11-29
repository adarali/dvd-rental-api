package com.example.rest.dvdrental.v2.config.security;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.InvalidCredentialsException;
import com.example.rest.dvdrental.v2.exceptions.user.AppUserNotFoundException;
import com.example.rest.dvdrental.v2.model.AuthRequest;
import com.example.rest.dvdrental.v2.model.AuthResponse;
import com.example.rest.dvdrental.v2.model.UserResponse;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.service.AuthTokenService;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppUserDetailsService implements UserDetailsService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AppUserService appUserService;
    private final AuthTokenService authTokenService;
    
    public AppUserDetailsService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AppUserService appUserService, AuthTokenService authTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.appUserService = appUserService;
        this.authTokenService = authTokenService;
    }
    
    @Override
    public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserService.findByUsername(username);
        return new AppUserDetails(user);
    }
    
    /**
     * Authenticates the user
     * @param request is an object that contains username and password
     * @return return a JWT token if the authentication is successful
     */
    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        AppUserDetails userDetails = null;
        try {
            userDetails = loadUserByUsername(request.getUsername());
        } catch (AppUserNotFoundException e) {
            throw new InvalidCredentialsException();
        }
        boolean matches = verifyCredentials(request, userDetails);
        if (userDetails.getUser().getSource() != null || !matches) {
            throw new InvalidCredentialsException();
        }
        
        String token = jwtUtil.generateToken(userDetails.getUser());
        
        return new AuthResponse(token, new UserResponse(userDetails.getUser()));
    }
    
    public boolean verifyCredentials(AuthRequest request) {
        return verifyCredentials(request, loadUserByUsername(request.getUsername()));
    }
    
    public boolean verifyCredentials(AuthRequest request, UserDetails userDetails) {
        return passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
    }
    
    public void logout(String token) {
        authTokenService.deleteToken(token);
    }
}
