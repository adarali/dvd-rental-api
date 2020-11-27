package com.example.rest.dvdrental.v2.utils;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.InvalidJwtTokenException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    
    private static final List<MacAlgorithm> PREFERRED_ALGS = Collections.unmodifiableList(Arrays.asList(
            MacAlgorithm.HS512, MacAlgorithm.HS256, MacAlgorithm.HS384
    ));
    
    @Value(value = "${jwt.timeout:60}")
    private Integer timeout;
    private AppSecretKey secretKey;
    private NimbusJwtDecoder jwtDecoder;
    
    public Date extractExpirationDate(String token) {
        Instant instant = jwtDecoder.decode(token).getExpiresAt();
        return Date.from(instant);
    }
    
    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }
    
    public String generateToken(AppUser user) {
        return generateToken(user, this.timeout);
    }
    
    public String generateToken(String username) {
        return generateToken(new AppUser(username), this.timeout);
    }
    
    public String generateToken(AppUser user, int timeout) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * timeout))
                .subject(user.getUsername())
                .claim("scope", user.getRole())
                .claim("role", user.getRole())
                .build();
        
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        
    
        Payload payload = new Payload(claims.toJSONObject());
        JWSHeader header = new JWSHeader(algorithm);
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(getSigningKey()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }
    
    public boolean isAdmin(String token) {
        return false;
    }
    
    public boolean isTokenValid(String token) {
        try {
            if(StringUtils.isBlank(token)) return false;
            String username = extractUsername(token);
            return username != null && !isTokenExpired(token);
        } catch (BadJwtException e) {
            return false;
        }
    }
    
    public void validateToken(String token) {
        if(!isTokenValid(token)) throw new InvalidJwtTokenException();
    }
    
    private SecretKey getSigningKey() {
        return secretKey;
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(getSigningKey()).macAlgorithm(this.secretKey.getMacAlgorithm()).build();
        this.jwtDecoder = decoder;
        return decoder;
    }
    
    @PostConstruct
    private void generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[128];
        random.nextBytes(bytes);
        this.secretKey = new AppSecretKey(bytes, getPreferredAlgorithm(bytes));
    }
    
    private MacAlgorithm getPreferredAlgorithm(byte[] bytes) {
        
        if (bytes == null) {
            throw new IllegalArgumentException("SecretKey byte array cannot be null.");
        }
        
        int bitLength = bytes.length * 8;
        
        for (MacAlgorithm alg : PREFERRED_ALGS) {
            if(bitLength >= getAlgLength(alg)) {
                return alg;
            }
        }
        
        String msg = "The specified key byte array is " + bitLength + " bits which " +
                "is not secure enough for any JWT HMAC-SHA algorithm.  The JWT " +
                "JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a " +
                "size >= 256 bits (the key size must be greater than or equal to the hash " +
                "output size)";
        throw new RuntimeException(msg);
    }
    
    private int getAlgLength(MacAlgorithm algorithm) {
        return Integer.valueOf(algorithm.getName().substring(2));
    }
}
