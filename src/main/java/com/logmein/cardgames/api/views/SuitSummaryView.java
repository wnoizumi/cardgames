package com.logmein.cardgames.api.views;

import java.util.UUID;

import com.logmein.cardgames.domain.entities.CardSuit;

public class SuitSummaryView {

	private CardSuit suit;
	private Long count;
	private UUID gameUuid;

	public SuitSummaryView(CardSuit suit, Long count, UUID gameUuid) {
		this.suit = suit;
		this.count = count;
		this.gameUuid = gameUuid;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public Long getCount() {
		return count;
	}

	public UUID getGameUuid() {
		return gameUuid;
	}
}
