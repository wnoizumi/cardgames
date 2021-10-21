package com.logmein.cardgames.api;

import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.logmein.cardgames.domain.exceptions.NoPlayingCardsToDealException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({NoSuchElementException.class})
	public ResponseEntity<Object> handleNoSuchElementException(
		      Exception ex, WebRequest request) {
		        return new ResponseEntity<Object>(
		          "Resource not found.", new HttpHeaders(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({NoPlayingCardsToDealException.class})
	public ResponseEntity<Object> handleNoPlayingCardsToDealException(
		      Exception ex, WebRequest request) {
		        return new ResponseEntity<Object>(
		          ex.getMessage(), new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
	}
}
