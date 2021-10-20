package com.logmein.cardgames.domain.exceptions;

public class NoPlayingCardsToDealException extends RuntimeException {

	public NoPlayingCardsToDealException(String message) {
        super(message);
    }
}
