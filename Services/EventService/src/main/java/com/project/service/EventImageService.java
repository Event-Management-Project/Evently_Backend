package com.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.dto.ApiResponse;

public interface EventImageService {

	ApiResponse addEventImage(List<MultipartFile> files, long evtId);

}
