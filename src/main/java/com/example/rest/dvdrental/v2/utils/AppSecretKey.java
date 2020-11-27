package com.example.rest.dvdrental.v2.utils;

import lombok.Getter;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

import javax.crypto.spec.SecretKeySpec;

public class AppSecretKey extends SecretKeySpec {
    
    @Getter
    private MacAlgorithm macAlgorithm;
    
    public AppSecretKey(byte[] key, MacAlgorithm algorithm) {
        super(key, algorithm.getName());
        this.macAlgorithm = algorithm;
    }
}
