package com.project.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.dao.BookingDAO;
import com.project.dao.TicketDAO;
import com.project.dto.ApiResponse;
import com.project.entities.Booking;
import com.project.entities.Ticket;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {
//	private final ModelMapper modelMapper;
	private final BookingDAO bookingDao;
	private final TicketDAO ticketDao;
	
	@Override
	public ApiResponse generateTicket(long bkg_id) {
		Booking booking=bookingDao.findById(bkg_id).orElseThrow();
		Ticket ticket=new Ticket();
		ticket.setDeleted(false);
		ticket.setIssueDate(LocalDateTime.now());
		ticket.setBookingId(booking);
		ticketDao.save(ticket);
		
		return new ApiResponse("Ticket SuccessFully Generated");
	}

}
