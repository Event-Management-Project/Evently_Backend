package com.project.dto;

//import com.sunbeam.entities.Category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventResponseDTO {
	private Long eventId;
	private Long orgId;
	private String eventTitle;
	private String description;
	private String startDateTime;
	private String endDateTime;
	private String location;
	private Long capacity;
	private double ticketPrice;
	private String categoryName;
	private String imageUrl;
}
