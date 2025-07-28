package com.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class OrganiserLoginDto {
	
	@NotBlank
	@Email
    private String email;
	
	@NotBlank
    private String password;
}
