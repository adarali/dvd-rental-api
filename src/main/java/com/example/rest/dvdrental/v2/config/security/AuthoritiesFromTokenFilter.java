package com.example.rest.dvdrental.v2.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class AuthoritiesFromTokenFilter extends OncePerRequestFilter {
    
    private final BearerTokenResolver resolver;
    private final UserDetailsService userDetailsService;
    private final JwtDecoder jwtDecoder;
    
    public AuthoritiesFromTokenFilter(BearerTokenResolver resolver, UserDetailsService userDetailsService, JwtDecoder jwtDecoder) {
        this.resolver = resolver;
        this.userDetailsService = userDetailsService;
        this.jwtDecoder = jwtDecoder;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolver.resolve(request);
        Collection<GrantedAuthority> authorities = null;
        if (token != null) {
            Jwt jwt = jwtDecoder.decode(token);
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwtDecoder.decode(token));
            authorities = authenticationToken.getAuthorities();
        }
        filterChain.doFilter(request, response);
    }
}
