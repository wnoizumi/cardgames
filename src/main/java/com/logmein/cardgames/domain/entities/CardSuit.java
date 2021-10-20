package com.logmein.cardgames.domain.entities;

public enum CardSuit {
	HEARTS(1), SPADES(2), CLUBS(3), DIAMONDS(4);

	private final int priority;

	private CardSuit(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return this.priority;
	}
}
