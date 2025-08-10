package com.project.external.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.project.dto.ApiResponse;
import com.project.external.entities.NotificationDTO;
import com.project.external.entities.Reviews;

@FeignClient(name = "NODE-SERVICE")
public interface NodeService {
	@PostMapping("/reviews/event")
	public ResponseEntity<List<Reviews>> getUserReviews(@RequestBody Map<String, Long> request);
	
	@PostMapping(value = "/notification/customer", consumes = "application/json")
    ApiResponse addNotificationCustomer(
        @RequestHeader("customerid") Long customerId,
        @RequestBody NotificationDTO notificationDTO
    );
    
    @PostMapping(value = "/notification/organiser", consumes = "application/json")
    ApiResponse addNotificationOrganiser(
            @RequestHeader("organiserid") Long organiserId,
            @RequestBody NotificationDTO notificationDTO
        );
}
