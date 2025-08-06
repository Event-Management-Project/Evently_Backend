package com.project.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Payment;
import com.project.entities.PaymentStatus;

public interface PaymentDAO extends JpaRepository<Payment,Long>{
	Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByBookingId_IdAndPaymentStatus(Long bookingId, PaymentStatus status);


}
