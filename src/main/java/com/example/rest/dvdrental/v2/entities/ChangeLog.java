package com.example.rest.dvdrental.v2.entities;

import com.example.rest.dvdrental.v2.enums.ChangeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChangeLog extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oldValue;
    private String newValue;
    private LocalDateTime changedDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
    @ManyToOne
    private Movie movie;
    
    public ChangeLog(String oldValue, String newValue, ChangeType changeType, Movie movie) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeType = changeType;
        this.movie = movie;
    }
}
