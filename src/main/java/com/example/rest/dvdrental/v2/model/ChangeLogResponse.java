package com.example.rest.dvdrental.v2.model;

import com.example.rest.dvdrental.v2.entities.ChangeLog;
import com.example.rest.dvdrental.v2.model.movie.MovieListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "Change Log Response", description = "This is the change log query result item")
@NoArgsConstructor
public class ChangeLogResponse {
    @Schema(description = "The movie that was modified")
    private MovieListItem movie;
    @Schema(description = "The date and time on which the movie was modified")
    private LocalDateTime date = LocalDateTime.now();
    @Schema(description = "The type of change which could be the sale price, the rental price or the title", example = "RENTAL_PRICE")
    private String changeType;
    @Schema(description = "The value before the change")
    private String oldValue;
    @Schema(description = "The value after the change")
    private String newValue;
    
    public ChangeLogResponse(ChangeLog changeLog) {
        this.movie = MovieListItem.from(changeLog.getMovie());
        this.date = changeLog.getChangedDate();
        this.changeType = changeLog.getChangeType().toString();
        this.oldValue = changeLog.getOldValue();
        this.newValue = changeLog.getNewValue();
    }
    
    
}
