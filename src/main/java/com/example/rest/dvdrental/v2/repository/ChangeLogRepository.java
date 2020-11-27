package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.ChangeLog;
import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.enums.ChangeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChangeLogRepository extends GenericRepository<ChangeLog, Long> {
    List<ChangeLog> findByMovie(Movie movie);
    
    @Query("select cl from ChangeLog cl where (cl.movie.id = :movieId or :movieId is null) " +
            "and cl.changedDate between :date1 and :date2 and (cl.changeType = :type or :type is null)")
    List<ChangeLog> queryLogs(Long movieId, LocalDateTime date1, LocalDateTime date2, ChangeType type);
    
}
