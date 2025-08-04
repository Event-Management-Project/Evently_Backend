package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private String id;
    private Long customerId;
    private Long eventId;
    private String subject;
    private String description;
    private int star;
}
