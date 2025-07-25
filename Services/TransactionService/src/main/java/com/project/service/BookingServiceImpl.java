package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.dao.BookingDAO;
import com.project.dto.ApiResponse;
import com.project.dto.BookingDTO;
import com.project.dto.BookingResponseDTO;
import com.project.entities.Booking;
import com.project.entities.BookingStatus;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional 
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{
	private final BookingDAO bookingDao;
	private final ModelMapper modelMapper;
	
	@Override
	public ApiResponse createBooking(BookingDTO bookingCreateDTO, long cstId, long eventId) {
        System.out.println("Creating booking with attendees: " + bookingCreateDTO.getTotalAttendee());

        Booking booking = modelMapper.map(bookingCreateDTO, Booking.class);
        booking.setBookingDate(LocalDateTime.now());
        booking.setCstId(cstId);
        booking.setEvtId(eventId);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setDeleted(false);

        bookingDao.save(booking);

        return new ApiResponse("Booking Created Successfully");
    }
	
	
	// Get Booking by User Id
	@Override
	public List<BookingResponseDTO> getBookingsByUserId(Long cstId) {
		List<Booking> booking=new ArrayList <Booking>();
		

		booking=bookingDao.findByCstId(cstId);
		List<BookingResponseDTO> bookingResponseList=new ArrayList<BookingResponseDTO>();
		
		BookingResponseDTO bookingResponse=null;
		
		for(Booking bookings:booking) {
			bookingResponse=modelMapper.map(bookings,BookingResponseDTO.class);
			bookingResponseList.add(bookingResponse);
		}
		
		return bookingResponseList;
	}
	
	@Override
	public BookingResponseDTO getBookingDetails(long bookingId) {
		BookingResponseDTO bookingDTO=new BookingResponseDTO();
		Optional<Booking> booking =bookingDao.findById(bookingId);
		bookingDTO=modelMapper.map(booking, BookingResponseDTO.class);
		return bookingDTO;
	}
	
	@Override
	public ApiResponse cancelBooking(long bookingId) {
	   Booking booking=bookingDao.findById(bookingId).orElseThrow();
	   booking.setDeleted(true);
	   bookingDao.save(booking);
	   return new ApiResponse("Booking Deleted");
	   
	}
	@Override
	public List<BookingResponseDTO> getBookingsByEventId(Long eventId) {
		 List<Booking> booking=new ArrayList<Booking>();
		    
	    booking=bookingDao.findByEvtId(eventId);
	    List<BookingResponseDTO>bookingResponseList=new ArrayList<BookingResponseDTO>();
	    
        BookingResponseDTO bookingResponse=null;
		
		for(Booking bookings:booking) {
			bookingResponse=modelMapper.map(bookings,BookingResponseDTO.class);
			bookingResponseList.add(bookingResponse);
		}
		
		return bookingResponseList;
	
		}
}
