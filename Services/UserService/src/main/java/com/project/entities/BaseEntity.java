package com.project.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(name="creation_date")
	private LocalDate creationDate;

	@UpdateTimestamp
	@Column(name="updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "phoneNumber",length = 10)
	@NotNull
	private String phoneNumber;

	 @Column(unique = true, nullable = false)
	@NotNull
	private String email;

	@Column(name = "password",length = 100)
	@NotNull
	private String password;

	@Column(name = "address")
	@NotNull
	private String address;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "is_deleted")
	private boolean isDeleted;

}
