package com.project.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganiserCreateDto {
    private String organiserCompanyName;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
    
    @Schema(type="String", format="binary")
	private MultipartFile image; // Field to hold the uploaded file
  }
