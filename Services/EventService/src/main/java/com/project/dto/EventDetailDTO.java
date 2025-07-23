package com.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventDetailDTO {
	private String eventTitle;
	private String description;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String location;
	private Long capacity;
	private Long remainingCapacity;
	private double ticketPrice;
	private String categoryName;
	
	private String imageUrl; // Store image as Base64 string
}
