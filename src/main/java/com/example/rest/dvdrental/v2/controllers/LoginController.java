package com.example.rest.dvdrental.v2.controllers;

import com.example.rest.dvdrental.v2.config.security.AppUserDetailsService;
import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.InvalidJwtTokenException;
import com.example.rest.dvdrental.v2.model.AuthRequest;
import com.example.rest.dvdrental.v2.model.AuthResponse;
import com.example.rest.dvdrental.v2.model.UserResponse;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/login/auth")
@Log
public class LoginController {
    
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;
    private final AppUserService appUserService;
    
    public LoginController(JwtUtil jwtUtil, AppUserDetailsService appUserDetailsService, AppUserService appUserService) {
        this.jwtUtil = jwtUtil;
        this.appUserDetailsService = appUserDetailsService;
        this.appUserService = appUserService;
    }
    
    @GetMapping
    public AuthResponse loginToken(@RequestParam String token) throws ParseException {
        try {
            jwtUtil.validateToken(token);
        } catch (InvalidJwtTokenException e) {
            log.info("invalid token: " + token);
            throw e;
        }
        String username = jwtUtil.extractUsername(token);
        AppUser user = appUserService.findByUsername(username);
        String newToken = jwtUtil.generateToken(user);
        return new AuthResponse(newToken, new UserResponse(user));
    }
    
    @PostMapping
    public AuthResponse loginUsernamePassword(@RequestBody AuthRequest authRequest) {
        AuthResponse response = appUserDetailsService.authenticate(authRequest);
        return response;
        
    }

}
