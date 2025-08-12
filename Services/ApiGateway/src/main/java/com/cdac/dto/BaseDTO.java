package com.cdac.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class BaseDTO {
	// add common fields - to be used in serialization i.e skip them during
	//de-serial , used only during ser.
	@JsonProperty(access =Access.READ_ONLY)
	private Long id;
	@JsonProperty(access =Access.READ_ONLY)
	private LocalDate creationDate;
	@JsonProperty(access =Access.READ_ONLY)
	private LocalDateTime updatedOn;
}
