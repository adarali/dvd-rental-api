package com.example.rest.dvdrental.v2.controllers;

import com.example.rest.dvdrental.v2.model.*;
import com.example.rest.dvdrental.v2.service.ChangeLogService;
import com.example.rest.dvdrental.v2.service.PurchaseService;
import com.example.rest.dvdrental.v2.service.RentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LogController {
    
    private final RentService rentService;
    private final PurchaseService purchaseService;
    private final ChangeLogService changeLogService;
    
    public LogController(RentService rentService, PurchaseService purchaseService, ChangeLogService changeLogService) {
        this.rentService = rentService;
        this.purchaseService = purchaseService;
        this.changeLogService = changeLogService;
    }
    
    @GetMapping("rents")
    @Secured("ROLE_ADMIN")
    public List<RentLogResponse> getRents(RentLogRequest request) {
        return rentService.getRentLogs(request);
    }
    
    @GetMapping("purchases")
    @Secured("ROLE_ADMIN")
    public List<PurchaseLogResponse> getPurchases(PurchaseLogRequest request) {
        return purchaseService.getLogs(request);
    }
    
    @GetMapping("changes")
    @Secured("ROLE_ADMIN")
    public List<ChangeLogResponse> getChanges(ChangeLogRequest request) {
        return changeLogService.getLogs(request);
    }
    
}
