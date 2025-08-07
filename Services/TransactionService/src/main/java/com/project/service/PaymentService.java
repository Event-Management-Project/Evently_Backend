package com.project.service;

import java.util.List;

import com.project.dto.ApiResponse;
import com.project.dto.PaymentDTO;

public interface PaymentService {

	ApiResponse makePayment(PaymentDTO paymentDto,long bkg_id);
	ApiResponse verifyPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature);
	List<PaymentDTO> getAllPayments();
	ApiResponse cancelPayment(long bookingId);
}