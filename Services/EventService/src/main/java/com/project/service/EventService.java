package com.project.service;

import java.util.List;
import java.util.Map;

//import org.springframework.http.ResponseEntity;

import com.project.dto.ApiResponse;
import com.project.dto.EventCreateDTO;
import com.project.dto.EventDetailDTO;
import com.project.dto.EventEditDTO;
import com.project.dto.EventResponseDTO;
import com.project.external.entities.Booking;
import com.project.external.entities.Customer;
import com.project.external.entities.CustomerReviews;
import com.project.external.entities.EventAttendee;
import com.project.external.entities.OrganiserDashboardDTO;
import com.project.external.entities.Reviews;

public interface EventService {

	EventResponseDTO addNewEvent(EventCreateDTO eventCreateDTO, Long organiser_id);

	List<EventResponseDTO> getUpcomingEvent();
	
	List<EventResponseDTO> getPastEvent();

	EventDetailDTO getEventDetails(Long evt_id);
	
	List<EventResponseDTO> getEventsByOrganiserId(Long organiser_id);

	List<EventResponseDTO> getUpcomingEventsByOrganiserId(Long organiser_id);

	List<EventResponseDTO> getPastEventsByOrganiserId(Long organiser_id);

	List<EventResponseDTO> getEventsByOrganiserIdAndSortedByDate(Long organiser_id);

	List<EventResponseDTO> getEventsByOrganiserIdAndFilteredByLocation(Long organiser_id, String location);

	List<EventResponseDTO> getEventsByOrganiserIdAndFilteredByCategory(Long organiser_id, Long category_id);

	ApiResponse deleteEvent(Long evt_id);

	ApiResponse editEventDetail(EventEditDTO eventDto, Long evt_id);

	List<EventResponseDTO> getAllEvents();

	List<EventAttendee> getEventAttendeesByOrganiserId(Long orgId);
	
	List<CustomerReviews> getCustomerReviews(Long orgId);
	
	// ****************************************************************
	// External Service Call
	
	List<Booking> getBookingByEventId(Long evtId);

	Customer getCustomerById(Long cstId);

	EventResponseDTO getEventById(Long evtId);

	List<Reviews> getReviewsByEventId(Long eventid);
	
	OrganiserDashboardDTO getOrganiserDashboard(Long orgId);

	Map<String, Long> getMonthlyEvents(Long orgId);

	Map<String, Long> getMonthlyRevenue(Long orgId);

	ApiResponse decrementCapacity(Long eventId, Long attendees);

	Boolean hasCapacity(Long eventId, Long attendees);
	
}
