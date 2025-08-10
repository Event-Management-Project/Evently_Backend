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
import com.project.dto.MonthlyEventsDTO;
import com.project.entities.Category;
import com.project.entities.EventImage;
import com.project.entities.Events;
import com.project.entities.Facility;
import com.project.external.entities.Booking;
import com.project.external.entities.Customer;
import com.project.external.entities.CustomerReviews;
import com.project.external.entities.EventAttendee;
import com.project.external.entities.NotificationDTO;
import com.project.external.service.BookingService;
import com.project.external.service.NodeService;
import com.project.external.entities.Organiser;
import com.project.external.entities.OrganiserDashboardDTO;
import com.project.external.entities.Reviews;
import com.project.external.service.UserService;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
	private final UserService userService;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Override
	public EventResponseDTO addNewEvent(EventCreateDTO eventCreateDTO, Long organiser_id) {
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

		EventResponseDTO eventResponse = modelMapper.map(event, EventResponseDTO.class);
		eventResponse.setEventId(event.getId());
		eventResponse.setOrgId(event.getOrganiserId());
		eventResponse.setCategoryName(event.getCategory().getCategoryName());

		EventImage eventImage = new EventImage();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = this.cloudinary.uploader().upload(eventCreateDTO.getFile().getBytes(),
					ObjectUtils.emptyMap());
			String url = (String) data.get("url");

			eventImage.setImageUrl(url);
			eventImage.setPrimary(true);
			eventImage.setEvent(event);

			eventImageDao.save(eventImage);
		} catch (IOException e) {
			throw new RuntimeException("File not been able to upload");
		}

		// Add Notification for Event Adding
		NotificationDTO notificationOrganiser = new NotificationDTO();
		notificationOrganiser.setSubject("Event Added Successfully");
		notificationOrganiser
				.setDescription("You have added the Event: " + eventCreateDTO.getEvent_title() + " into your shelf");

		nodeService.addNotificationOrganiser(organiser_id, notificationOrganiser);

		return eventResponse;
	}

	@Override
	public List<EventResponseDTO> getUpcomingEvent() {
		LocalDateTime currentDate = LocalDateTime.now();

		List<EventResponseDTO> eventResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByStartDateTimeAfterAndIsDeletedFalse(currentDate);
		EventResponseDTO eventResponse = null;

		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			eventResponse.setOrgId(event.getOrganiserId());

			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

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

		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());

			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

			eventResponseDto.add(eventResponse);
		}

		return eventResponseDto;
	}

	// Fetch Events By Id
	@Override
	public EventDetailDTO getEventDetails(Long evtId) {
		Events event = eventdao.findById(evtId).orElseThrow();
		Category category = event.getCategory();

		EventDetailDTO eventResponse = modelMapper.map(event, EventDetailDTO.class);
		eventResponse.setCategoryName(category.getCategoryName());
		eventResponse.setEventId(evtId);

		// Fetching Image Urls
		List<EventImage> eventImages = eventImageDao.findByEvent(event);
		List<String> imageUrls = new ArrayList<>();
		for (EventImage eventImage : eventImages) {
			String url = eventImage.getImageUrl();

			imageUrls.add(url);
		}

		eventResponse.setImageUrl(imageUrls);

		// Fetching facilities
		List<Facility> facilities = event.getFacilities();
		List<String> facililityList = new ArrayList<>();

		for (Facility facility : facilities) {
			String facilityName = new String();
			facilityName = facility.getFacilityName();

			facililityList.add(facilityName);
		}
		eventResponse.setFacilities(facililityList);

		// fetching Organiser
		Organiser organiser = userService.getOrganiser(event.getOrganiserId()).getBody();
		eventResponse.setOrganiserCompany(organiser.getOrganiserCompanyName());
		eventResponse.setOrganiserEmail(organiser.getEmail());
		eventResponse.setOrganiserPhone(organiser.getPhoneNumber());
		eventResponse.setOrganiserAddress(organiser.getAddress());

		return eventResponse;
	}

	// Fetch Events By OrganiserId
	@Override
	public List<EventResponseDTO> getEventsByOrganiserId(Long organiser_id) {
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndIsDeletedFalse(organiser_id);

		for (Events event : events) {
			EventResponseDTO eventResponse = modelMapper.map(event, EventResponseDTO.class);

			Category category = event.getCategory();
			if (category != null) {
				eventResponse.setCategoryName(category.getCategoryName());
			} else {
				eventResponse.setCategoryName("Uncategorized"); // Or set null
			}

			eventResponse.setEventId(event.getId());

			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}
			eventsResponseDto.add(eventResponse);
		}

		return eventsResponseDto;
	}

	// Fetch Upcoming Events By OrganiserId
	@Override
	public List<EventResponseDTO> getUpcomingEventsByOrganiserId(Long organiser_id) {
		LocalDateTime currentDate = LocalDateTime.now();

		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndStartDateTimeAfterAndIsDeletedFalse(organiser_id,
				currentDate);

		EventResponseDTO eventResponse = null;
		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());

			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

			eventsResponseDto.add(eventResponse);
		}

		return eventsResponseDto;
	}

	// Fetch Past Events By OrganiserId
	@Override
	public List<EventResponseDTO> getPastEventsByOrganiserId(Long organiser_id) {
		LocalDateTime currentDate = LocalDateTime.now();

		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByOrganiserIdAndStartDateTimeBeforeAndIsDeletedFalse(organiser_id,
				currentDate);

		EventResponseDTO eventResponse = null;
		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

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
		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

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
		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category category = event.getCategory();

			eventResponse.setCategoryName(category.getCategoryName());
			eventResponse.setEventId(event.getId());
			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

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
		for (Events event : events) {
			eventResponse = modelMapper.map(event, EventResponseDTO.class);
			Category categoryName = event.getCategory();

			eventResponse.setCategoryName(categoryName.getCategoryName());
			eventResponse.setEventId(event.getId());
			EventImage primaryImage = eventImageDao.findByEventIdAndIsPrimaryTrue(event.getId());
			if (primaryImage != null) {
				eventResponse.setImageUrl(primaryImage.getImageUrl());
			} else {
				eventResponse.setImageUrl(null);
			}

			eventsResponseDto.add(eventResponse);
		}

		return eventsResponseDto;
	}

	// Delete Event By ID
	@Override
	public ApiResponse deleteEvent(Long evt_id) {
		LocalDateTime currentDate = LocalDateTime.now();

		Events event = eventdao.findByIdAndStartDateTimeAfter(evt_id, currentDate)
				.orElseThrow(() -> new RuntimeException("Event not found or has already started — cannot delete past events."));

		event.setDeleted(true);
		
		List<Booking> bookingList = bookingService.getBookingByEvent(evt_id).getBody();
		
		if (bookingList != null) {
	        for (Booking booking : bookingList) {
	            try {
	                bookingService.cancelBooking(booking.getId());
	                bookingService.cancelPayment(booking.getId());
	            } catch (Exception e) {
	                System.err.println("Failed to cancel booking/payment for ID " + booking.getId());
	            }
	        }
	    }
		
		eventdao.save(event);

		return new ApiResponse("Event and related bookings/payments cancelled successfully");
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
		
		if (files != null && !files.isEmpty()) {
	        eventImageService.addEventImage(files, evt_id);
	    }
		
		eventdao.save(event);

		return new ApiResponse("Event Updated");
	}

	// Get All Events
	@Override
	public List<EventResponseDTO> getAllEvents() {
		List<Events> eventList = eventdao.findAll();

		List<EventResponseDTO> eventResponseList = new ArrayList<>();
		EventResponseDTO eventResponseDTO = null;

		for (Events event : eventList) {
			eventResponseDTO = modelMapper.map(event, EventResponseDTO.class);

			Category category = event.getCategory();

			eventResponseDTO.setCategoryName(category.getCategoryName());
			eventResponseDTO.setEventId(event.getId());
			eventResponseDTO.setOrgId(event.getOrganiserId());

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
		eventResponse.setEndDateTime(event.getEndDateTime().toString());
		eventResponse.setDescription(event.getDescription());
		eventResponse.setEventId(evtId);
		eventResponse.setOrgId(event.getOrganiserId());

		return eventResponse;
	}

	// Fetch Event Attendee:
	public List<EventAttendee> getEventAttendeesByOrganiserId(Long orgId) {
		List<EventAttendee> eventAttendeeList = new ArrayList<>();

		if (orgId == null) {
			throw new IllegalArgumentException("Organiser ID cannot be null.");
		}

		List<EventResponseDTO> eventList = getEventsByOrganiserId(orgId);
		if (eventList == null || eventList.isEmpty()) {
			return eventAttendeeList;
		}

		for (EventResponseDTO event : eventList) {
			if (event == null || event.getEventId() == null) {
				continue;
			}

			Long evtId = event.getEventId();
			List<Booking> bookingList = getBookingByEventId(evtId);
			if (bookingList == null || bookingList.isEmpty()) {
				continue;
			}

			for (Booking booking : bookingList) {
				if (booking == null || !"CONFIRMED".equals(booking.getStatus())) {
					continue;
				}

				Long cstId = booking.getCstId();
				if (cstId == null) {
					continue;
				}

				Customer customer = getCustomerById(cstId);
				if (customer == null)
					continue;

				EventAttendee eventAttendee = new EventAttendee();
				eventAttendee.setCustomerName(customer.getCustomerName());
				eventAttendee.setEventTitle(event.getEventTitle());
				eventAttendee.setEmail(customer.getEmail());
				eventAttendee.setAttendeeCount(booking.getTotalAttendee());
				eventAttendee.setTicketPrice(event.getTicketPrice() * booking.getTotalAttendee());
				eventAttendee.setLocation(customer.getAddress());

				eventAttendeeList.add(eventAttendee);
			}
		}

		return eventAttendeeList;
	}

	@Override
	public List<CustomerReviews> getCustomerReviews(Long orgId) {
		List<CustomerReviews> customerReviewsList = new ArrayList<>();

		List<EventResponseDTO> eventResponseList = getEventsByOrganiserId(orgId);

		for (EventResponseDTO eventResponse : eventResponseList) {

			try {
				Events event = eventdao.findById(eventResponse.getEventId())
						.orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventResponse.getEventId()));

				List<Reviews> reviewList = getReviewsByEventId(event.getId());
				if (reviewList == null || reviewList.isEmpty()) {
					return customerReviewsList;
				}

				for (Reviews review : reviewList) {
					try {
						String cstId = review.getCustomerId();
						if (cstId == null || cstId.isEmpty()) {
							continue;
						}

						Long customerId = Long.valueOf(cstId);
						Customer customer = getCustomerById(customerId);
						if (customer == null) {
							continue;
						}

						CustomerReviews customerReviews = new CustomerReviews();
						customerReviews.setCustomerName(customer.getCustomerName());
						customerReviews.setEmail(customer.getEmail());
						customerReviews.setEventTitle(event.getEventTitle());
						customerReviews.setStar(review.getStar());
						customerReviews.setSubject(review.getSubject());
						customerReviews.setDescription(review.getDescription());

						customerReviewsList.add(customerReviews);

					} catch (NumberFormatException e) {
						System.err.println("Invalid customer ID format: " + review.getCustomerId());
					} catch (Exception e) {
						System.err.println("Error processing review: " + e.getMessage());
					}
				}

			} catch (Exception e) {
				System.err.println("Error fetching customer reviews: " + e.getMessage());
			}
		}

		return customerReviewsList;
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
		ResponseEntity<Customer> customerResponse = userService.getCustomerById(cstId);

		return customerResponse.getBody();
	}

	@Override
	public List<Reviews> getReviewsByEventId(Long eventid) {
		Map<String, Long> body = new HashMap<>();
		body.put("eventId", eventid);
		
		ResponseEntity<List<Reviews>> reviewList = nodeService.getUserReviews(body);

		return reviewList.getBody();
	}

	@Override
	public OrganiserDashboardDTO getOrganiserDashboard(Long orgId) {
        List<Events> events = eventdao.findByOrganiserIdAndIsDeletedFalse(orgId);
        
        long totalEvents = events.size();
        LocalDateTime now = LocalDateTime.now();

        long activeEvents = events.stream()
                .filter(e -> !e.getStartDateTime().isAfter(now) && !e.getEndDateTime().isBefore(now))
                .count();

        long pastEvents = events.stream()
                .filter(e -> e.getEndDateTime().isBefore(now))
                .count();
        
        double totalRevenue = 0;
        long totalTicketsSold = 0;
        
        for (Events event : events) {
            List<Booking> bookings = bookingService.getBookingByEvent(event.getId()).getBody();

            if (bookings != null) {
                for (Booking booking : bookings) {
                    totalTicketsSold += booking.getTotalAttendee();
                    totalRevenue += booking.getTotalAttendee() * event.getTicketPrice();
                }
            }
        }

        return new OrganiserDashboardDTO(totalEvents, activeEvents, pastEvents, totalRevenue, totalTicketsSold);
	}

	@Override
	public Map<String, Long> getMonthlyEvents(Long orgId) {
		List<Events> events = eventdao.findByOrganiserIdAndIsDeletedFalse(orgId);

		Map<String, Long> countPerMonth = events.stream()
			    .collect(Collectors.groupingBy(
			        e -> (String) e.getStartDateTime().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
			        Collectors.counting()
			    ));
		
		return countPerMonth;
	}

	@Override
	public Map<String, Long> getMonthlyRevenue(Long orgId) {
	    List<Events> events = eventdao.findByOrganiserIdAndIsDeletedFalse(orgId);

	    Map<String, Long> revenuePerMonth = new HashMap<>();

	    for (Events event : events) {
	        // Fetch bookings for this event using Feign client
	        ResponseEntity<List<Booking>> response = bookingService.getBookingByEvent(event.getId());
	        List<Booking> bookings = response.getBody();

	        if (bookings != null) {
	            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	            for (Booking booking : bookings) {
	                String bookingDateStr = booking.getBookingDate();
	                if (bookingDateStr != null && booking.getTotalAttendee() != null) {
	                    try {
	                        LocalDateTime bookingDate = LocalDateTime.parse(bookingDateStr, formatter);

	                        long revenue = Math.round(event.getTicketPrice() * booking.getTotalAttendee());

	                        String month = bookingDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

	                        revenuePerMonth.merge(month, revenue, Long::sum);
	                    } catch (DateTimeParseException e) {
	                        System.err.println("Invalid booking date format: " + bookingDateStr);
	                    }
	                }
	            }
	        }

	    }

	    return revenuePerMonth;
	}

}