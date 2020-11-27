package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.ChangeLog;
import com.example.rest.dvdrental.v2.enums.ChangeType;
import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import com.example.rest.dvdrental.v2.model.ChangeLogRequest;
import com.example.rest.dvdrental.v2.model.ChangeLogResponse;
import com.example.rest.dvdrental.v2.repository.ChangeLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChangeLogService extends AbstractService<ChangeLog, Long> {
    
    private final ChangeLogRepository repo;
    
    public ChangeLogService(ChangeLogRepository repo) {
        this.repo = repo;
    }
    
    @Override
    protected ChangeLogRepository getRepo() {
        return repo;
    }
    
    public List<ChangeLogResponse> getLogs(ChangeLogRequest request) {
        if (request.getDateFrom() == null || request.getDateTo() == null) {
            throw new AppValidationException("You have to provide both the initial and the final dates");
        }
        ChangeType changeType = null;
        if (Stream.of(ChangeType.values())
                .anyMatch(type -> type.toString().equals(request.getChangeType()))) {
            changeType = ChangeType.valueOf(request.getChangeType());
        }
        
        return getRepo().queryLogs(
                request.getMovieId(),
                request.getDateFrom(),
                request.getDateTo(),
                changeType).stream().map(cl -> new ChangeLogResponse(cl))
                .collect(Collectors.toList());
    }
}
