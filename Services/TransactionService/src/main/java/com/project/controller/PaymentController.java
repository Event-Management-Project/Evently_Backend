package com.project.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.PaymentDTO;
import com.project.dto.ApiResponse; // ✅ Corrected import
import com.project.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:5173/")
@AllArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;

	@PostMapping("/{bkg_id}")
	public ResponseEntity<?> makePayment(@RequestBody PaymentDTO paymentDto, @PathVariable long bkg_id) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(paymentService.makePayment(paymentDto, bkg_id));
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) {
	    String razorpayPaymentId = payload.get("razorpayPaymentId");
	    String razorpayOrderId = payload.get("razorpayOrderId");
	    String razorpaySignature = payload.get("razorpaySignature");

	    ApiResponse response = paymentService.verifyPayment(
	        razorpayPaymentId, razorpayOrderId, razorpaySignature
	    );

	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/cancel/{bookingId}")
	public ResponseEntity<ApiResponse> cancelPayment(@PathVariable long bookingId){
		return ResponseEntity.status(HttpStatus.OK)
				.body(paymentService.cancelPayment(bookingId));
	}
}
