package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reviews {
	private String _id;
	private String customerId;
	private String eventId;
	private String subject;
	private String description;
	private Long star;
	private Long __v;
}
