package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventResponseDTO {
	private Long eventId;
	private Long orgId;
	private String eventTitle;
	private String description;
	private String startDateTime;
	private String endDateTime;
	private String location;
	private Long capacity;
	private Long remainingCapacity;
	private double ticketPrice;
	private String categoryName;
}
