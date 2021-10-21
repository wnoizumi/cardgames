package com.logmein.cardgames.api.views;

import com.logmein.cardgames.domain.entities.CardSuit;

public class SuitSummaryView {

	private CardSuit suit;
	private Long count;

	public SuitSummaryView(CardSuit suit, Long count) {
		this.suit = suit;
		this.count = count;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public Long getCount() {
		return count;
	}
}
