package com.project.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.external.entities.EventResponseDTO;

@FeignClient(name = "EventService", url = "http://localhost:9090", configuration = FeignClientConfiguration.class)
public interface EventService {
	@GetMapping("/event/byEventId/{evtId}")
	public ResponseEntity<EventResponseDTO> getEventById(@PathVariable("evtId") Long evtId);
}
