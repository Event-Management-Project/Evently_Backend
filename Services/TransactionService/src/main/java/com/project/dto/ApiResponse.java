package com.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper=true)
@NoArgsConstructor
public class ApiResponse {
	private String msg;
	private LocalDateTime curTime;
    private Object data;

    public ApiResponse(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

	
	public ApiResponse(String message) {
		this.msg = message;
		this.curTime = LocalDateTime.now();
	}
}
