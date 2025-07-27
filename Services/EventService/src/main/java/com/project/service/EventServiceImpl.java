package com.project.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.dao.CategoryDao;
import com.project.dao.EventDao;
import com.project.dao.EventImageDao;
import com.project.dto.ApiResponse;
import com.project.dto.EventCreateDTO;
import com.project.dto.EventResponseDTO;
import com.project.entities.Category;
import com.project.entities.EventImage;
import com.project.entities.Events;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
public class EventServiceImpl implements EventService {
	private final ModelMapper modelMapper;
	private final EventDao eventdao;
	private final CategoryDao categoryDao;
	private final EventImageDao eventImageDao;
	private final Cloudinary cloudinary;
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
		
		return new ApiResponse("New Event Added");
	}
	
	

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

	@Override
	public EventDetailDTO getEventDetails(Long evt_id) {
		Optional<Events> event = eventdao.findByIdAndIsDeletedFalse(evt_id);
		EventDetailDTO eventResponse = modelMapper.map(event, EventDetailDTO.class);
				
		EventImage eventImage = eventImageDao.findByEventAndIsPrimaryTrue(event.get());
		String imageUrl = eventImage.getImageUrl();
		
		eventResponse.setImageUrl(imageUrl);
		
		return eventResponse;
	}

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

	@Override
	public List<EventResponseDTO> getEventDetailByOrganiserId(Long evt_id, Long organiser_id) {
		List<EventResponseDTO> eventsResponseDto = new ArrayList<>();
		List<Events> events = eventdao.findByIdAndOrganiserIdAndIsDeletedFalse(evt_id, organiser_id);
		
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

	@Override
	public ApiResponse deleteEvent(Long evt_id) {
		LocalDateTime currentDate = LocalDateTime.now(); 

		Events event = eventdao.findByIdAndStartDateTimeAfter(evt_id, currentDate).orElseThrow(() -> new RuntimeException("Event Cannot be deleted"));
		
		event.setDeleted(true);
		eventdao.save(event);
		
		return new ApiResponse("Event deleted successfully");
	}

	@Override
	public ApiResponse editEventDetail(EventCreateDTO eventDto, Long evt_id) {
		
		
		return null;
	}

// Get All Events
	@Override
	public List<EventResponseDTO> getAllEvents() {
		List<Events> eventList = eventdao.findAll();
		
		List<EventResponseDTO> eventResponseList = new ArrayList<>();
		EventResponseDTO eventResponseDTO = null;
		
		for(Events event: eventList) {
			eventResponseDTO = modelMapper.map(event, EventResponseDTO.class);
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
}
