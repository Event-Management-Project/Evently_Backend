package com.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.project.dto.ApiResponse;
import com.project.dto.EventCreateDTO;
import com.project.dto.EventResponseDTO;
import com.project.service.EventService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {
	
	private final EventService eventService;
	
	@PostMapping(value = "/addEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> addNewEvent(@ModelAttribute EventCreateDTO eventCreateDTO,  @RequestParam Long organiser_id){
		 
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.addNewEvent(eventCreateDTO, organiser_id));
	}
	
	@GetMapping("/upcomingEvent")
	public ResponseEntity<List<EventResponseDTO>> getUpcomingEvent(){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getUpcomingEvent());
	}
	
	@GetMapping("/pastEvent")
	public ResponseEntity<List<EventResponseDTO>> getPastEvent(){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getPastEvent());
	}
}