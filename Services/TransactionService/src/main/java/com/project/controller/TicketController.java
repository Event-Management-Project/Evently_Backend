package com.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.service.TicketService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/ticket")
@AllArgsConstructor
public class TicketController {
	private final TicketService ticketService;
	
	@PostMapping("/{bkg_id}")
	public ResponseEntity<?> generateTicket(@PathVariable long bkg_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ticketService.generateTicket(bkg_id));
	}
}
