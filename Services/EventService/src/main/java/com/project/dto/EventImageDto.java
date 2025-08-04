package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventImageDto {
    private Long id;
    private String imageUrl;
    private boolean isPrimary;
    private String uploadedBy;
    private Long eventId;
}
