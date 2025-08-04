package com.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper=true)
public class ApiResponse {
	private String msg;
	private LocalDateTime curTime;
	
	public ApiResponse(String message) {
		this.msg = message;
		this.curTime = LocalDateTime.now();
	}
	
	public ApiResponse() {
        this.curTime = LocalDateTime.now();
    }
}
