package com.project.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table
@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseEntity{
	
	@Column(name="cst_id")
	private Long cstId;
	
	@Column(name="evt_id")
	private Long evtId;
	
	@Column(name="total_attendee")
	private Long totalAttendee;
	
	@Column(name="bkg_date")
	private LocalDateTime bookingDate;
	
	@Enumerated
	private BookingStatus status;
}
