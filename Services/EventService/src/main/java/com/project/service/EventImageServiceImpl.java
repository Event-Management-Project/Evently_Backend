package com.project.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.dao.EventDao;
import com.project.dao.EventImageDao;
import com.project.dto.ApiResponse;
import com.project.dto.EventImageDto;
import com.project.entities.EventImage;
import com.project.entities.Events;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class EventImageServiceImpl implements EventImageService {
	private final Cloudinary cloudinary;
	private final EventDao eventDao;
	private final EventImageDao eventImageDao;
	
	private final ModelMapper modelMapper;
    
    @Override
	public ApiResponse addEventImage(List<MultipartFile> files, long evtId) {
	    Events event = eventDao.findById(evtId).orElseThrow();

	    try {
	        for (MultipartFile image : files) {
	            @SuppressWarnings("unchecked")
	            Map<String, Object> data = this.cloudinary.uploader()
	                .upload(image.getBytes(), ObjectUtils.emptyMap());

	            String url = (String) data.get("url");

	            EventImage eventImage = new EventImage();
	            eventImage.setImageUrl(url);
	            eventImage.setPrimary(false);
	            eventImage.setEvent(event);

	            eventImageDao.save(eventImage);
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("File could not be uploaded", e);
	    }

	    return new ApiResponse("Event Images Added");
	}

    @Override
    public List<EventImageDto> getImagesForEvent(Long eventId) {
        List<EventImage> images = eventImageDao.findByEventId(eventId);
        return images.stream()
                     .map(image -> modelMapper.map(image, EventImageDto.class))
                     .toList();
    }

    @Override
    public EventImageDto getPrimaryImageForEvent(Long eventId) {
        EventImage image = eventImageDao.findByEventIdAndIsPrimaryTrue(eventId);
        if (image == null) return null;
        return modelMapper.map(image, EventImageDto.class);
    }
}
