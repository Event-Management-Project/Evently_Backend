package com.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.project.dto.ApiResponse;
import com.project.dto.EventCreateDTO;
import com.project.dto.EventDetailDTO;
import com.project.dto.EventEditDTO;
import com.project.dto.EventResponseDTO;
import com.project.external.entities.EventAttendee;
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

	@GetMapping("/eventDetail/{evtId}")
	public ResponseEntity<EventDetailDTO> getEventDetail(@PathVariable Long evtId){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getEventDetails(evtId));
	}
	
	@GetMapping("/organiserEvent")
	public ResponseEntity<List<EventResponseDTO>> getEventsByOrganiserId(@RequestParam Long organiserId){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getEventsByOrganiserId(organiserId));
	}
	
	@GetMapping("/organiserUpcomingEvent")
	public ResponseEntity<List<EventResponseDTO>> getUpcomingEventsByOrganiserId(@RequestParam Long organiser_id){
		return ResponseEntity.status(HttpStatus.OK)
				.body(eventService.getUpcomingEventsByOrganiserId(organiser_id));
	}
	
	@GetMapping("/organiserPastEvent")
	public ResponseEntity<List<EventResponseDTO>> getPastEventsByOrganiserId(@RequestParam Long organiser_id){
		return ResponseEntity.status(HttpStatus.OK)
				.body(eventService.getPastEventsByOrganiserId(organiser_id));
	}
	
	@GetMapping("/organiserEventByDate")
	public ResponseEntity<List<EventResponseDTO>> getEventsByOrganiserIdAndSortedByDate(@RequestParam Long organiser_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getEventsByOrganiserIdAndSortedByDate(organiser_id));
	}
	
	@GetMapping("/organiserEventByLocation/{location}")
	public ResponseEntity<List<EventResponseDTO>> getEventsByOrganiserIdAndFilteredByLocation(@RequestParam Long organiser_id, @PathVariable String location){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getEventsByOrganiserIdAndFilteredByLocation(organiser_id, location));
	}
	
	@GetMapping("/organiserEventByCategory/{category_id}")
	public ResponseEntity<List<EventResponseDTO>> getEventsByOrganiserIdAndFilteredByCategory(@RequestParam Long organiser_id, @PathVariable Long category_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.getEventsByOrganiserIdAndFilteredByCategory(organiser_id, category_id));
	}
	
	@PutMapping(value = "/editEvent/{evt_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> editEventDetail(@ModelAttribute EventEditDTO eventDto, @PathVariable Long evt_id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(eventService.editEventDetail(eventDto, evt_id));
	}
	
	@DeleteMapping("/deleteEvent/{evt_id}")
	public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long evt_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(eventService.deleteEvent(evt_id));
	}
	
	
	@GetMapping("/allEvents")
		public ResponseEntity<List<EventResponseDTO>> getAllEvents(){
			return ResponseEntity.status(HttpStatus.OK)
					.body(eventService.getAllEvents());
		}
		
	@GetMapping("/byEventId/{evtId}")
	public ResponseEntity<EventResponseDTO> getEventById (@PathVariable Long evtId){
		return ResponseEntity.status(HttpStatus.OK)
				.body(eventService.getEventById(evtId));
	}
	
	// Event Attendee List consisting of [Customer Name , Email from Customer Service], 
	// [attendeeCount and Price form booking service], [Location from Event Service] 
	@GetMapping("/eventAttendee")
	public ResponseEntity<List<EventAttendee>> getAllEventAttendee(@RequestParam Long orgId) {
	    List<EventAttendee> eventAttendeeList = eventService.getEventAttendeesByOrganiserId(orgId);
	    return ResponseEntity.status(HttpStatus.OK)
	            .body(eventAttendeeList);
	}
}