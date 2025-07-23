package com.project.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

//import com.sunbeam.entities.Category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventCreateDTO {
	private String event_title;
	private String description;
	private String startDateTime;
	private String endDateTime;
	private String location;
	private Long capacity;
	private double ticketPrice;
	private String categoryName;
	
	@Schema(type="String", format="binary")
	private MultipartFile file; // Field to hold the uploaded file
}
