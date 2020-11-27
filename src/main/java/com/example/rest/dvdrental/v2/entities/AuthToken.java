package com.example.rest.dvdrental.v2.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthToken extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String token;
    private LocalDateTime expirationDate;
    private String username;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }
    
    public boolean isValid() {
        return !isExpired();
    }
}
