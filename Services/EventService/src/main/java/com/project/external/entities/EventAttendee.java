package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventAttendee {
	private String customerName;
	private String email;
	private Long attendeeCount;
	private double ticketPrice;
	private String location;
}
