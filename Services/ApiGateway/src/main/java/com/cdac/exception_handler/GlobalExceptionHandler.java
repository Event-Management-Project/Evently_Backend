package com.cdac.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cdac.dto.ApiResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {
	
	// add exception handling method - to handle authentication failure
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
		System.out.println("in catch -invalid authentication");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)// SC 401
				.body(new ApiResponse(e.getMessage()));
	}
	
//	// add exception handling method - to handle authorization failure
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
//		System.out.println("in catch access denied exc "+e);
//	
//		return ResponseEntity.status(HttpStatus.FORBIDDEN)// SC 403
//				.body(new ApiResponse(e.getMessage()));
//		
//	}


	
	// add exception handling method - to handle any pending exceptions
		@ExceptionHandler(Exception.class)
		public ResponseEntity<?> handleException(Exception e) {
			System.out.println("in catch all");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// SC 500
					.body(new ApiResponse(e.getMessage()));
		}
	


	
}
