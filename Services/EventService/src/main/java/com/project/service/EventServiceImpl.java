package com.project.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.dao.CategoryDao;
import com.project.dao.EventDao;
import com.project.dao.EventImageDao;
import com.project.dto.ApiResponse;
import com.project.dto.EventCreateDTO;
import com.project.dto.EventDetailDTO;
import com.project.dto.EventEditDTO;
import com.project.dto.EventResponseDTO;
import com.project.entities.Category;
import com.project.entities.EventImage;
import com.project.entities.Events;
import com.project.external.entities.Booking;
import com.project.external.entities.Customer;
import com.project.external.entities.EventAttendee;
import com.project.external.entities.NotificationDTO;
import com.project.external.service.BookingService;
import com.project.external.service.CustomerService;
import com.project.external.service.NodeService;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class EventServiceImpl implements EventService {
	private final ModelMapper modelMapper;
	private final EventDao eventdao;
	private final CategoryDao categoryDao;
	private final EventImageDao eventImageDao;
	private final Cloudinary cloudinary;
	private final EventImageService eventImageService;
	private final NodeService nodeService;
	private final BookingService bookingService;
	private final CustomerService customerService;
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	// Add New Event with Primary Image for the Event
	@Override
	public ApiResponse addNewEvent(EventCreateDTO eventCreateDTO, Long organiser_id) {
		Events event = new Events();
		Category category = categoryDao.findByCategoryName(eventCreateDTO.getCategoryName());
		
		LocalDateTime startDateTime = LocalDateTime.parse(eventCreateDTO.getStartDateTime(), FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(eventCreateDTO.getEndDateTime(), FORMATTER);
		
		event = modelMapper.map(eventCreateDTO, Events.class);
		
		event.setRemainingCapacity(event.getCapacity());
		event.setOrganiserId(organiser_id);
		event.setCategory(category);
		event.setEventTitle(eventCreateDTO.getEvent_title());
		event.setStartDateTime(startDateTime);
		event.setEndDateTime(endDateTime);
				
		eventdao.save(event);
		
		EventImage eventImage = new EventImage();
		try {
        	@SuppressWarnings("unchecked")
			Map<String, Object> data = this.cloudinary.uploader().upload(eventCreateDTO.getFile().getBytes(), ObjectUtils.emptyMap());
        	String url = (String) data.get("url");
        	
        	eventImage.setImageUrl(url);
        	eventImage.setPrimary(true);
        	eventImage.setEvent(event);
        	
        	eventImageDao.save(eventImage);
        }
        catch(IOException e) {
        	throw new RuntimeException("File not been able to upload");
        }
		
		// Add Notification for Event Adding
		NotificationDTO notificationOrganiser = new NotificationDTO();
		notificationOrganiser.setSubject("Event Added Successfully");
		notificationOrganiser.setDescription("You have added the Event: "+eventCreateDTO.getEvent_title() 
				+ "into your shelf");
		
		nodeService.addNotificationOrganiser(organiser_id, notificationOrganiser);
		
		
		return new ApiResponse("New Event Added");
	}
	
	// Fetch Upcoming Events
	@Override
	public List<EventResponseDTO> getUpcomingEvent() {
		LocalDateTime currentDate = LocalDateTime.now(); 
		
		List<EventResponseDTO> eventResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByStartDateTimeAfterAndIsDeletedFalse(currentDate);
		EventResponseDTO eventResponse = null;
		
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventResponseDto.add(eventResponse);
		}
		
		return eventResponseDto;
	}

	// Fetch Past Events
	@Override
	public List<EventResponseDTO> getPastEvent() {
		LocalDateTime currentDate = LocalDateTime.now(); 
		
		List<EventResponseDTO> eventResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByStartDateTimeBeforeAndIsDeletedFalse(currentDate);
		EventResponseDTO eventResponse = null;
		
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());

			eventResponseDto.add(eventResponse);
		}
		
		return eventResponseDto;
	}

	// Fetch Events By Id
	@Override
	public EventDetailDTO getEventDetails(Long evt_id) {
		Optional<Events> event = eventdao.findByIdAndIsDeletedFalse(evt_id);
		EventDetailDTO eventResponse = modelMapper.map(event, EventDetailDTO.class);
				
		EventImage eventImage = eventImageDao.findByEventAndIsPrimaryTrue(event.get());
		String imageUrl = eventImage.getImageUrl();
		
		eventResponse.setImageUrl(imageUrl);
		
		return eventResponse;
	}

	// Fetch Events By OrganiserId
	@Override
	public List<EventResponseDTO> getEventsByOrganiserId(Long organiser_id) {
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		
		List<Events> events = eventdao.findByOrganiserIdAndIsDeletedFalse(organiser_id);
		EventResponseDTO eventResponse = null;
		
		for (Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventsResponseDto.add(eventResponse);
		}

		return eventsResponseDto;
	}

	// Fetch Upcoming Events By OrganiserId
	@Override
	public List<EventResponseDTO> getUpcomingEventsByOrganiserId(Long organiser_id) {
		LocalDateTime currentDate = LocalDateTime.now();
		
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndStartDateTimeAfterAndIsDeletedFalse(organiser_id, currentDate);
		
		EventResponseDTO eventResponse = null;
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventsResponseDto.add(eventResponse);
		}
		
		return eventsResponseDto;
	}

	// Fetch Past Events By OrganiserId
	@Override
	public List<EventResponseDTO> getPastEventsByOrganiserId(Long organiser_id) {
		LocalDateTime currentDate = LocalDateTime.now();
		
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndStartDateTimeBeforeAndIsDeletedFalse(organiser_id, currentDate);
		
		EventResponseDTO eventResponse = null;
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventsResponseDto.add(eventResponse);
		}
		
		return eventsResponseDto;
	}

	// Fetch Events By OrganiserId and Sorted By Date
	@Override
	public List<EventResponseDTO> getEventsByOrganiserIdAndSortedByDate(Long organiser_id) {		
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndIsDeletedFalseOrderByStartDateTimeAsc(organiser_id);
		
		EventResponseDTO eventResponse = null;
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());	
			eventsResponseDto.add(eventResponse);
		}
		
		return eventsResponseDto;
	}

	// Fetch Events By OrganiserId and Filtered By Location
	@Override
	public List<EventResponseDTO> getEventsByOrganiserIdAndFilteredByLocation(Long organiser_id, String location) {
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndLocationAndIsDeletedFalse(organiser_id, location);
		
		EventResponseDTO eventResponse = null;
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventsResponseDto.add(eventResponse);
		}
		
		return eventsResponseDto;
	}

	// Fetch Events By OrganiserId and Filtered By Category
	@Override
	public List<EventResponseDTO> getEventsByOrganiserIdAndFilteredByCategory(Long organiser_id, Long category_id) {
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		Optional<Category> category = categoryDao.findById(category_id);
		
		List<Events> events = eventdao.findByOrganiserIdAndCategoryAndIsDeletedFalse(organiser_id, category);
		
		
		EventResponseDTO eventResponse = null;
		for(Events event: events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category categoryName = event.getCategory();

			eventResponse.setCategoryName(categoryName.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventsResponseDto.add(eventResponse);
		}
		
		return eventsResponseDto;
	}

	// Delete Event By ID
	@Override
	public ApiResponse deleteEvent(Long evt_id) {
		LocalDateTime currentDate = LocalDateTime.now(); 

		Events event = eventdao.findByIdAndStartDateTimeAfter(evt_id, currentDate).orElseThrow(() -> new RuntimeException("Event Cannot be deleted"));
		
		event.setDeleted(true);
		eventdao.save(event);
		
		return new ApiResponse("Event deleted successfully");
	}

	// Update Event
	@Override
	public ApiResponse editEventDetail(EventEditDTO eventDto, Long evt_id) {
	    Optional<Events> optionalEvent = eventdao.findById(evt_id);

	    if (!optionalEvent.isPresent()) {
	        return new ApiResponse("Event not found with ID: " + evt_id);
	    }

	    Events event = optionalEvent.get();

	    modelMapper.map(eventDto, event);

	    Category category = categoryDao.findByCategoryName(eventDto.getCategoryName());

	    LocalDateTime startDateTime = LocalDateTime.parse(eventDto.getStartDateTime(), FORMATTER);
	    LocalDateTime endDateTime = LocalDateTime.parse(eventDto.getEndDateTime(), FORMATTER);

	    event.setCategory(category);
	    event.setStartDateTime(startDateTime);
	    event.setEndDateTime(endDateTime);
	    event.setRemainingCapacity(eventDto.getCapacity());
	    event.setEventTitle(eventDto.getEvent_title());
	    
	    List<MultipartFile> files = eventDto.getFiles();
	    eventImageService.addEventImage(files, evt_id);

	    eventdao.save(event);

	    return new ApiResponse("Event Updated");
	}

	// Get All Events
	@Override
	public List<EventResponseDTO> getAllEvents() {
		List<Events> eventList = eventdao.findAll();
		
		List<EventResponseDTO> eventResponseList = new ArrayList<>();
		EventResponseDTO eventResponseDTO = null;
		
		for(Events event: eventList) {
			eventResponseDTO = modelMapper.map(event, EventResponseDTO.class);
			
			Category category = event.getCategory();
			
			eventResponseDTO.setCategoryName(category.getCategoryName());
			eventResponseDTO.setEventId(event.getId());
			
			try {
	            EventImage eventImage = eventImageDao.findByEventAndIsPrimaryTrue(event);
	            if (eventImage != null) {
	                String imageUrl = eventImage.getImageUrl();
	                eventResponseDTO.setImageUrl(imageUrl);
	            } else {
	                eventResponseDTO.setImageUrl(null);
	            }
	        } catch (Exception e) {
	            eventResponseDTO.setImageUrl(null);
	        }
			
			eventResponseList.add(eventResponseDTO);
		}
		
		return eventResponseList;
	}

	@Override
	public EventResponseDTO getEventById(Long evtId) {
		Events event = eventdao.findById(evtId).orElseThrow();
		Category category = event.getCategory();

		EventResponseDTO eventResponse = modelMapper.map(event, EventResponseDTO.class);
		eventResponse.setCategoryName(category.getCategoryName());
		eventResponse.setEventId(evtId);
		
		return eventResponse;
	}


	// Fetch Event Attendee: 
	public List<EventAttendee> getEventAttendeesByOrganiserId(Long orgId) {
	    List<EventAttendee> eventAttendeeList = new ArrayList<>();

	    List<EventResponseDTO> eventList = getEventsByOrganiserId(orgId);

	    for (EventResponseDTO event : eventList) {
	        Long evtId = event.getEventId();
	        List<Booking> bookingList = getBookingByEventId(evtId);
	        for (Booking booking : bookingList) {
	            Long cstId = booking.getCstId();
	            EventAttendee eventAttendee = new EventAttendee();
	            Customer customer = getCustomerById(cstId);

	            eventAttendee.setCustomerName(customer.getCustomerName());
	            eventAttendee.setEmail(customer.getEmail());
	            eventAttendee.setAttendeeCount(booking.getTotalAttendee());
	            eventAttendee.setTicketPrice(event.getTicketPrice() * booking.getTotalAttendee());
	            eventAttendee.setLocation(customer.getAddress());
	            eventAttendeeList.add(eventAttendee);
	        }
	    }
	    return eventAttendeeList;
	}
	
	// ****************************************
	// External Service call
	
	
	@Override
	public List<Booking> getBookingByEventId(Long evtId) {
		ResponseEntity<List<Booking>> bookingListResponse = bookingService.getBookingByEvent(evtId);
		List<Booking> bookingList = bookingListResponse.getBody();
		
		return bookingList;
	}

	@Override
	public Customer getCustomerById(Long cstId) {
		ResponseEntity<Customer> customerResponse = customerService.getCustomerById(cstId);
		
		return customerResponse.getBody();
	}
}
