package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistory {
	private String eventTitle;
	private String location;
	private String category;
	private String bookingDate;
	private Long attendee;
	private double price;
	private String Status;
}
