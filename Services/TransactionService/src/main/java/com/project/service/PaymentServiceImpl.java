package com.project.service;

import java.time.LocalDateTime;
import java.util.List;

import com.project.utils.Utils;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.dao.BookingDAO;
import com.project.dao.PaymentDAO;
import com.project.dto.ApiResponse;
import com.project.dto.PaymentDTO;
import com.project.entities.Booking;
import com.project.entities.BookingStatus;
import com.project.entities.Payment;
import com.project.entities.PaymentStatus;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
	@Value("${razorpay.api.key}")
	private String apiKey;

	@Value("${razorpay.api.secret}")
	private String apiSecret;

	private final ModelMapper modelMapper;
	private final BookingDAO bookingDao;
	private final PaymentDAO paymentDao;

	public PaymentServiceImpl(ModelMapper modelMapper, BookingDAO bookingDao, PaymentDAO paymentDao) {
		this.modelMapper = modelMapper;
		this.bookingDao = bookingDao;
		this.paymentDao = paymentDao;
	}

	@Override
	public ApiResponse makePayment(PaymentDTO dto, long bkg_id) {
	    try {
	        RazorpayClient razorPayClient = new RazorpayClient(apiKey, apiSecret);

	        Booking booking = bookingDao.findById(bkg_id)
	            .orElseThrow(() -> new RuntimeException("Booking not found"));

	        JSONObject options = new JSONObject();
	        options.put("amount", (int) (dto.getAmount() * 100));
	        options.put("currency", "INR");
	        options.put("receipt", "receipt_" + System.currentTimeMillis());

	        Order order = razorPayClient.orders.create(options);

	        Payment payment = new Payment();
	        payment.setAmount(dto.getAmount());
	        payment.setBookingId(booking);
	        payment.setAttendeeCount(dto.getAttendeeCount());
	        payment.setRazorpayOrderId(order.get("id"));
	        payment.setPaymentStatus(PaymentStatus.PENDING);
	        payment.setPaymentMethod(dto.getPaymentMethod());
	        payment.setPaymentDate(LocalDateTime.now());

	        paymentDao.save(payment);

	        return new ApiResponse("Order created successfully", order.toString());

	    } catch (Exception e) {
	        return new ApiResponse("Failed to create order: " + e.getMessage());
	    }
	}


	@Override
	public ApiResponse verifyPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
	    try {
	        String generatedSignature = Utils.calculateHMAC(razorpayOrderId + "|" + razorpayPaymentId, apiSecret);

	        if (!generatedSignature.equalsIgnoreCase(razorpaySignature)) {
	            return new ApiResponse("Invalid payment signature");
	        }

	        Payment payment = paymentDao.findByRazorpayOrderId(razorpayOrderId)
	                .orElseThrow(() -> new RuntimeException("Payment order not found"));

	        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
	            return new ApiResponse("Payment already verified");
	        }

	        payment.setPaymentStatus(PaymentStatus.COMPLETED);
	        payment.setPaymentDate(LocalDateTime.now());
	        paymentDao.save(payment);

	        Booking booking = payment.getBookingId();
	        booking.setStatus(BookingStatus.CONFIRMED);
	        bookingDao.save(booking);

	        return new ApiResponse("Payment verified and booking confirmed.");
	    } catch (Exception e) {
	        return new ApiResponse("Payment verification failed: " + e.getMessage());
	    }
	}


	@Override
	public List<PaymentDTO> getAllPayments() {
		List<Payment> payments = paymentDao.findAll();
		return payments.stream().map(payment -> modelMapper.map(payments, PaymentDTO.class)).toList();

	}

}
