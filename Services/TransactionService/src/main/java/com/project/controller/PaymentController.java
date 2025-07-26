package com.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.PaymentDTO;
import com.project.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
	@PostMapping("/{bkg_id}")
	public ResponseEntity<?> makePayment(@RequestBody PaymentDTO paymentDto,@PathVariable long bkg_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(paymentService.makePayment(paymentDto,bkg_id));
	}
}
