package com.example.rest.dvdrental.v2.utils;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.InvalidJwtTokenException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.java.Log;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Log
public class JwtUtil {
    
    private static final List<MacAlgorithm> PREFERRED_ALGS = Collections.unmodifiableList(Arrays.asList(
            MacAlgorithm.HS512, MacAlgorithm.HS256, MacAlgorithm.HS384
    ));
    
    @Value(value = "${jwt.timeout:60}")
    private Integer timeout;
    @Value(value = "${jwt.secret}")
    private String jwtSecret;
    private AppSecretKey secretKey;
    private JwtDecoder jwtDecoder;
    
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
        return generateToken(user, timeout, getSigningKey());
    }
    
    public String generateToken(AppUser user, int timeout, AppSecretKey secretKey) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * timeout))
                .subject(user.getUsername())
                .claim("scope", user.getRole())
                .claim("role", user.getRole())
                .build();
        
        JWSAlgorithm algorithm = JWSAlgorithm.parse(secretKey.getMacAlgorithm().getName());
        
    
        Payload payload = new Payload(claims.toJSONObject());
        JWSHeader header = new JWSHeader(algorithm);
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
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
    
    public void validatePasswordToken(String token, JwtDecoder decoder) {
        try {
            Jwt jwt = decoder.decode(token);
        } catch (JwtException e) {
            log.severe(e.getMessage());
            throw new InvalidJwtTokenException();
        }
    }
    
    private AppSecretKey getSigningKey() {
        return secretKey;
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        this.jwtDecoder = getJwtDecoder(secretKey);
        return this.jwtDecoder;
    }
    
    public JwtDecoder getJwtDecoder(String secret) {
        return getJwtDecoder(generateSecretKey(secret));
    }
    
    public JwtDecoder getJwtDecoder(AppSecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(secretKey.getMacAlgorithm()).build();
    }
    
    @PostConstruct
    private void init() {
        this.secretKey = generateSecretKey();
    }
    
    public AppSecretKey generateSecretKey() {
        return generateSecretKey("");
    }
    
    public AppSecretKey generateSecretKey(String secret) {
        byte bytes[] = DigestUtils.sha512(StringUtils.join(secret, this.jwtSecret).getBytes());
        return new AppSecretKey(bytes, getPreferredAlgorithm(bytes));
    }
    
    public AppSecretKey generatePasswordSecretKey(AppUser user) {
        String s = StringUtils.join(user.getUsername(), user.getPassword());
        return generateSecretKey(s);
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
