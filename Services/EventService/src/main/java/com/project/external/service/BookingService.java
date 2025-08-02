package com.project.external.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.external.entities.Booking;

@FeignClient(name = "TransactionService", url = "http://localhost:9092", configuration = FeignClientConfiguration.class)
public interface BookingService {
	
	@GetMapping("/api/bookings/event/{eventId}")
	public ResponseEntity<List<Booking>> getBookingByEvent(@PathVariable Long eventId);
}
