package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BookingResponseDTO {

	private Long id;
	private Long cstId;
	private Long evtId;
	private Long totalAttendee;
	private String bookingDate;
	private String status;
}
