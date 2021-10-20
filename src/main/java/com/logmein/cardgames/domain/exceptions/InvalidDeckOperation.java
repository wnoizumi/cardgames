package com.logmein.cardgames.domain.exceptions;

public class InvalidDeckOperation extends RuntimeException {

	public InvalidDeckOperation(String message) {
		super(message);
	}
}
