package com.example.rest.dvdrental.v2.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id", callSuper = false)
@NoArgsConstructor
public class PasswordRecoveryToken extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token = RandomStringUtils.randomAlphanumeric(20);
    private LocalDateTime expiration = LocalDateTime.now().plusDays(1);
    private boolean enabled = true;
    @ManyToOne
    private AppUser user;
    
    public PasswordRecoveryToken(AppUser user) {
        this.user = user;
    }
    
    public boolean isExpired() {
        return expiration.isBefore(LocalDateTime.now());
    }
    
    public boolean isValid() {
        return isEnabled() && !isExpired();
    }
}
