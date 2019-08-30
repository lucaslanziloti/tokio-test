package com.example.api.exception.handler;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.api.exception.ErrorResponse;

@RestControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse illegalStateExceptionHandler(IllegalStateException exception) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now(), exception.getMessage());
	}
	
}