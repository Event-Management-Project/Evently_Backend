package com.project.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString(callSuper = true, exclude = {"events"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
	
	@Column(name="ctg_name")
	private String categoryName;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="is_deleted")
	private boolean isDeleted;
	
	@JsonIgnore
	@OneToMany(mappedBy = "category")
    private List<Events> events = new ArrayList<>();
}
