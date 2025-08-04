package com.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.dto.ApiResponse;
import com.project.dto.EventImageDto;
import com.project.entities.EventImage;

public interface EventImageService {
	ApiResponse addEventImage(List<MultipartFile> files, long evtId);

	public List<EventImageDto> getImagesForEvent(Long eventId);

	public EventImageDto getPrimaryImageForEvent(Long eventId);
}
