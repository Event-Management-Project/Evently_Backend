package com.project.service;

import java.util.List;

//import org.springframework.http.ResponseEntity;

import com.project.dto.ApiResponse;
import com.project.dto.EventCreateDTO;
import com.project.dto.EventResponseDTO;

public interface EventService {

	ApiResponse addNewEvent(EventCreateDTO eventCreateDTO, Long organiser_id);

	List<EventResponseDTO> getUpcomingEvent();
	
	List<EventResponseDTO> getPastEvent();
	
}
