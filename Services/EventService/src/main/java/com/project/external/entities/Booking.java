package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
	private Long id;
	private Long cstId;
	private Long evtId;
	private Long totalAttendee;
	private String bookingDate;
	private String status;
}
