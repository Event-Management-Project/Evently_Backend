package com.project.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.ApiResponse;
import com.project.external.entities.EventResponseDTO;

@FeignClient(name = "EVENTSERVICE")
public interface EventService {
	@GetMapping("/event/byEventId/{evtId}")
	public ResponseEntity<EventResponseDTO> getEventById(@PathVariable("evtId") Long evtId);
	
	@GetMapping("/event/{evtId}/has-capacity")
    ResponseEntity<Boolean> hasCapacity(
        @PathVariable("evtId") Long evtId,
        @RequestParam("attendees") Long attendees
    );
	
	@PutMapping("/event/{evtId}/decrement-capacity")
    ResponseEntity<ApiResponse> decrementCapacity(
            @PathVariable("evtId") Long evtId,
            @RequestParam("attendees") Long attendees
    );
}
