package com.project.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
public class Facility extends BaseEntity {
	@Column(name="fct_name")
	private String facilityName;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="is_deleted")
	private boolean isDeleted;
	
	@ManyToMany(mappedBy = "facilities")
    private List<Events> events = new ArrayList<>();
}
