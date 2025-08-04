package com.project.dto;

import com.project.entities.PaymentMethod;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentDTO {
	private String razorpayOrderId;
	private PaymentMethod paymentMethod;
	private double amount;
	private long attendeeCount;
	
}