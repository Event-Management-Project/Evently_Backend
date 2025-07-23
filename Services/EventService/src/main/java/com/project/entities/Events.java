package com.project.entities;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="events")
@ToString(callSuper = true, exclude= {"category", "facilities"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Events extends BaseEntity {
	@Column(name="evt_title")
	private String eventTitle;
	
	@Column(length = 100)
	private String description;
	
	@Column(name="start_date_time")
	private LocalDateTime startDateTime;
	
	@Column(name="end_date_time")
	private LocalDateTime endDateTime;
	
	private String location;
	
	private Long capacity;
	
	@Column(name="remaining_capacity")
	private Long remainingCapacity;
	
	@Column(name="ticket_price")
	private double ticketPrice;
	
	@ManyToMany
	@JoinTable(name="event_facility",
	joinColumns= @JoinColumn(name="evt_id"),
	inverseJoinColumns = @JoinColumn(name="fct_id"))
	private List<Facility> facilities = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@Column(name="organiser_id")
	private Long organiserId;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="is_deleted")
	private boolean isDeleted;
}
