package com.project.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.ApiResponse;
import com.project.dto.EventImageDto;
import com.project.entities.EventImage;
import com.project.service.EventImageService;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@RestController
@RequestMapping("/eventImage")
public class EventImageController {
    private final EventImageService eventImageService;

    @GetMapping("/{eventId}")
    public List<EventImageDto> getEventImages(@PathVariable Long eventId) {
        return eventImageService.getImagesForEvent(eventId);
    }
}
