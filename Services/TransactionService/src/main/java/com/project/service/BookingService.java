package com.project.service;

import java.util.List;

import com.project.dto.ApiResponse;
import com.project.dto.BookingDTO;
import com.project.dto.BookingResponseDTO;
import com.project.external.entities.BookingHistory;
import com.project.external.entities.EventResponseDTO;

public interface BookingService {
    
    ApiResponse createBooking(BookingDTO bookingCreateDTO, long cstId, long eventId);

    List<BookingResponseDTO> getBookingsByUserId(Long cstId);

    BookingResponseDTO getBookingDetails(long bookingId);

    ApiResponse cancelBooking(long bookingId);

    List<BookingResponseDTO> getBookingsByEventId(Long eventId);
    
	EventResponseDTO getEventById(Long evtId);

	List<BookingHistory> getBookingHistoryByUserId(Long cstId);
}
