package com.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.project.entities.Facility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventDetailDTO {
	private Long eventId;
	private String eventTitle;
	private String description;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String location;
	private Long capacity;
	private Long remainingCapacity;
	private double ticketPrice;
	private String categoryName;
	
	private List<String> imageUrl;
	private List<String> facilities;
	
	private String organiserCompany;
	private String organiserEmail;
	private String organiserPhone;
	private String organiserAddress;
}
