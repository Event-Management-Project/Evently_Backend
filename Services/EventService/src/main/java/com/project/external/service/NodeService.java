package com.project.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.project.dto.ApiResponse;
import com.project.external.entities.NotificationDTO;

@FeignClient(name = "NODE-SERVICE", url = "http://localhost:4000")
public interface NodeService {
	
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
