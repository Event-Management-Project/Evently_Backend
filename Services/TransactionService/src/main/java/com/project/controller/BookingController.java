package com.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ApiResponse;
import com.project.dto.BookingDTO;
import com.project.dto.BookingResponseDTO;
import com.project.service.BookingService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@AllArgsConstructor
public class BookingController {
	
	private final BookingService bookingService;
	
	//New Booking
	 @PostMapping(value = "/{cstId}/{eventId}")
	    public ResponseEntity<ApiResponse> createBooking(
	            @RequestBody BookingDTO dto,
	            @PathVariable long cstId,
	            @PathVariable long eventId) {

	        System.out.println("Received booking: " + dto);
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(bookingService.createBooking(dto, cstId, eventId));
	    }
	
	//Get Bookings By User ID
	@GetMapping("/customer")
	public ResponseEntity<List<BookingResponseDTO>> getBookingByUserId(@RequestParam Long cstId){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(bookingService.getBookingsByUserId(cstId));
	}
	
	@GetMapping("/{bookingId}")
	public ResponseEntity<?> getBookingDetails(@PathVariable long bookingId){
		return ResponseEntity.ok(bookingService.getBookingDetails(bookingId));
    }
	
	//Cancel a Booking
	@DeleteMapping("/{bookingId}")
	public ResponseEntity<?> cancelBooking(@PathVariable long bookingId){
		return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
	}
	
	//Get Bookings By Event
	@GetMapping("/event/{eventId}")
	public ResponseEntity<?> getBookingsByEvent(@PathVariable long eventId){
		return ResponseEntity.ok(bookingService.getBookingsByEventId(eventId));
	}
}
	
