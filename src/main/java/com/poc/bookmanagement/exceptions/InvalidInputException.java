package com.poc.bookmanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class InvalidInputException extends RuntimeException {

	public InvalidInputException(String message) {
		super(message);
	}
	
	
}
