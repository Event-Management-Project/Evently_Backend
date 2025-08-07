package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReviews {
	private String customerName;
	private String email;
	private String eventTitle;
	private String subject;
	private String description;
	private Long star;
}
