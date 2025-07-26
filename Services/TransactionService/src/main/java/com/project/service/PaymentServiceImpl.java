package com.project.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.dao.BookingDAO;
import com.project.dao.PaymentDAO;
import com.project.dto.ApiResponse;
import com.project.dto.PaymentDTO;
import com.project.entities.Booking;
import com.project.entities.Payment;
import com.project.entities.PaymentStatus;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final ModelMapper modelMapper;
	private final BookingDAO bookingDao;
	private final PaymentDAO paymentDao;
	
	@Override
	public ApiResponse makePayment(PaymentDTO paymentDto,long bkg_id) {
		Booking booking=bookingDao.findById(bkg_id).orElseThrow();
		Payment payment=modelMapper.map(paymentDto, Payment.class);
		payment.setDeleted(false);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setBookingId(booking);
		payment.setPaymentStatus(PaymentStatus.COMPLETED);
		paymentDao.save(payment);
		
		return new ApiResponse("Payment Successful");
		
		
	}

}
