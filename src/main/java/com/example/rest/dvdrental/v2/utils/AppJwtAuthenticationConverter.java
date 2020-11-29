package com.example.rest.dvdrental.v2.utils;

import lombok.val;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AppJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        val scopes = jwtGrantedAuthoritiesConverter.convert(source);
        List<String> authorities = source.getClaimAsStringList("scope");
        if(authorities == null) authorities = Collections.emptyList();
        return new JwtAuthenticationToken(source, authorities.stream().map(a -> new SimpleGrantedAuthority("ROLE_"+a)).collect(Collectors.toList()));
    }
}
