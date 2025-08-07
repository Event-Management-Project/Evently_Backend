package com.project.external.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.project.dto.ApiResponse;
import com.project.external.entities.Booking;

@FeignClient(name = "TRANSACTIONSERVICE")
public interface BookingService {
	
	@GetMapping("/bookings/event/{eventId}")
	public ResponseEntity<List<Booking>> getBookingByEvent(@PathVariable Long eventId);
	
	@PutMapping("/bookings/cancel/{bookingId")
	public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long bookingId);
	
	@PutMapping("/payment/cancel/{bookingId")
	public ResponseEntity<ApiResponse> cancelPayment(@PathVariable Long bookingId);
}
