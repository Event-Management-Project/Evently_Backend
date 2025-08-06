package com.project.service;

import java.util.List;
import java.util.Optional;

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
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ModelMapper modelMapper;

  @Value("${razorpay.api.key}") 
  private String apiKey;

  @Value("${razorpay.api.secret}") 
  private String apiSecret;

  private final BookingDAO bookingDao;
  private final PaymentDAO paymentDao;
  private final TicketService ticketService;

 

  @Override
  public ApiResponse makePayment(PaymentDTO dto, long bkgId) {
    Optional<Payment> pending = paymentDao.findByBookingId_IdAndPaymentStatus(bkgId, PaymentStatus.PENDING);
    if (pending.isPresent()) {
      Payment pendingPayment = pending.get();
      return new ApiResponse("Pending order exists", "{\"id\":\"" + pendingPayment.getRazorpayOrderId() + "\"}");
    }

    try {
      RazorpayClient client = new RazorpayClient(apiKey, apiSecret);

      Booking booking = bookingDao.findById(bkgId)
          .orElseThrow(() -> new RuntimeException("Booking not found"));

      JSONObject options = new JSONObject();
      options.put("amount", (int) (dto.getAmount() * 100)); 
      options.put("currency", "INR");
      options.put("receipt", "receipt_" + System.currentTimeMillis());

      Order order = client.orders.create(options);

      Payment payment = new Payment();
      payment.setBookingId(booking);
      payment.setAmount(dto.getAmount());
      payment.setAttendeeCount(dto.getAttendeeCount());
      payment.setRazorpayOrderId(order.get("id"));
      payment.setPaymentStatus(PaymentStatus.PENDING);
      paymentDao.save(payment);

      return new ApiResponse("Order created", order.toString());

    } catch (RazorpayException e) {
      e.printStackTrace();
      return new ApiResponse("Payment initiation failed: " + e.getMessage());
    }
  }

  @Override
  public ApiResponse verifyPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
    try {
      String generatedSignature = Utils.calculateHMAC(razorpayOrderId + "|" + razorpayPaymentId, apiSecret);
      if (!generatedSignature.equals(razorpaySignature)) {
        return new ApiResponse("Invalid signature");
      }

      Payment payment = paymentDao.findByRazorpayOrderId(razorpayOrderId)
          .orElseThrow(() -> new RuntimeException("Payment not found"));

      if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
        return new ApiResponse("Already verified");
      }

      payment.setPaymentStatus(PaymentStatus.COMPLETED);
      paymentDao.save(payment);

      Booking booking = payment.getBookingId();
      booking.setStatus(BookingStatus.CONFIRMED);
      bookingDao.save(booking);

      ticketService.generateTicket(booking.getId());

      return new ApiResponse("Payment verified & booking confirmed");

    } catch (Exception e) {
      e.printStackTrace();
      return new ApiResponse("Verification failed: " + e.getMessage());
    }
  }

  @Override
	public List<PaymentDTO> getAllPayments() {
		List<Payment> payments = paymentDao.findAll();
		return payments.stream().map(payment -> modelMapper.map(payment, PaymentDTO.class)).toList();
	}
}
