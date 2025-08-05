package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.dao.BookingDAO;
import com.project.dto.ApiResponse;
import com.project.dto.BookingDTO;
import com.project.dto.BookingResponseDTO;
import com.project.entities.Booking;
import com.project.entities.BookingStatus;
import com.project.external.entities.BookingHistory;
import com.project.external.entities.EventResponseDTO;
import com.project.external.service.EventService;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional 
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{
	private final BookingDAO bookingDao;
	private final ModelMapper modelMapper;
	
	private final EventService eventService;
	
	@Override
	public ApiResponse createBooking(BookingDTO bookingCreateDTO, long cstId, long eventId) {
	    Booking booking = modelMapper.map(bookingCreateDTO, Booking.class);
	    booking.setBookingDate(LocalDateTime.now());
	    booking.setCstId(cstId);
	    booking.setEvtId(eventId);
	    booking.setStatus(BookingStatus.PENDING);
	    booking.setDeleted(false);

	    bookingDao.save(booking);

	    BookingResponseDTO responseDTO = modelMapper.map(booking, BookingResponseDTO.class);

	    // Optional: convert date to string format if needed
	    responseDTO.setBookingDate(booking.getBookingDate().toString());

	    return new ApiResponse("Booking Created Successfully", responseDTO);
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
	
	// ******************************************
	// Making MicroService Call
	
	@Override
	public EventResponseDTO getEventById(Long evtId) {
		try {
			System.out.println("Event ");
	        ResponseEntity<EventResponseDTO> eventResponse = eventService.getEventById(evtId);
	        System.out.println(eventResponse.getStatusCode());
	        if (eventResponse.getStatusCode() == HttpStatus.OK) {
	        	System.out.println(eventResponse.getBody());
	            return eventResponse.getBody();
	        }
	        throw new RuntimeException("Event not found for evtId: " + evtId);
	    } catch (FeignException.NotFound ex) {
	        throw new RuntimeException("Event not found for evtId: " + evtId, ex);
	    }
	}
	
	public List<BookingHistory> getBookingHistoryByUserId(Long cstId) {
	    List<BookingHistory> bookingHistoryList = new ArrayList<>();
	    List<BookingResponseDTO> bookingList = getBookingsByUserId(cstId); // Calling Internal Booking Service
	    System.out.println(bookingList.toString());

	    for (BookingResponseDTO booking : bookingList) {
	        Long evtId = booking.getEvtId();

	        if (evtId == null) {
	            continue;
	        }
	        try {
	            EventResponseDTO eventResponse = getEventById(evtId); // Calling External Event Service
	            BookingHistory bookingHistory = new BookingHistory();
	            bookingHistory.setEventTitle(eventResponse.getEventTitle());
	            bookingHistory.setLocation(eventResponse.getLocation());
	            bookingHistory.setCategory(eventResponse.getCategoryName());
	            bookingHistory.setBookingDate(booking.getBookingDate());
	            bookingHistory.setAttendee(booking.getTotalAttendee());
	            bookingHistory.setPrice(booking.getTotalAttendee() * eventResponse.getTicketPrice());
	            bookingHistory.setStatus(booking.getStatus());
	            bookingHistoryList.add(bookingHistory);
	        } catch (RuntimeException ex) {
	            System.err.println("Failed to fetch event for evtId: " + evtId + ", error: " + ex.getMessage());
	            continue;
	        }
	    }
	    return bookingHistoryList;
	}
	
	
	
}
