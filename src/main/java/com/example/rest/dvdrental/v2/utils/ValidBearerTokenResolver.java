package com.example.rest.dvdrental.v2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Spring Security doesn't treat invalid tokens as non-authenticated users. This class is to make Spring Security do so
 */
@Component
public class ValidBearerTokenResolver implements BearerTokenResolver {
    
    private final DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
    private final JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    
    public ValidBearerTokenResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public String resolve(HttpServletRequest request) {
        String token = null;
        try {
            token = defaultBearerTokenResolver.resolve(request);
            if(!jwtUtil.isTokenValid(token)) return null;
        } catch (OAuth2AuthenticationException e) {
            return null;
        }
        
        return token;
    }
    
    public String resolve() {
        return resolve(this.request);
    }
}
