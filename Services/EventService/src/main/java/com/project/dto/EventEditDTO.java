package com.project.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventEditDTO {
	private String event_title;
	private String description;
	private String startDateTime;
	private String endDateTime;
	private String location;
	private Long capacity;
	private double ticketPrice;
	private String categoryName;
	
	private List<MultipartFile> files;
}
