package com.project.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Payment extends BaseEntity {
	@OneToOne
	@JoinColumn(name="bkg_id")
	private Booking bookingId;
	
	@Column(name = "razorpay_order_id")
	private String razorpayOrderId;
	
	@Column(name="pmt_date")
	private LocalDateTime paymentDate;
	
	@Column(name="attendee_count")
	private long attendeeCount;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="payment_method")
	private PaymentMethod paymentMethod;
	
	@Column(name="status")
	private PaymentStatus paymentStatus;
}
