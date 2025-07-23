package com.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="event_images")
@ToString(callSuper = true)
@Getter
@Setter
@JsonIgnoreProperties("creationDate")
public class EventImage extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name="evt_id")
	private Events event;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@Column(name="is_primary")
	private boolean isPrimary;
	
	@Column(name="uploaded_by")
	private String uploadedBy;
}
