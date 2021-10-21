package com.logmein.cardgames.api.views;

import java.util.UUID;

import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;

public class SuitFaceSummaryView implements Comparable<SuitFaceSummaryView> {

	private CardSuit suit;
	private CardFace face;
	private Long count;
	private UUID gameUuid;

	public SuitFaceSummaryView(CardSuit suit, CardFace face, Long count, UUID gameUuid) {
		this.suit = suit;
		this.face = face;
		this.count = count;
		this.gameUuid = gameUuid;
	}

	public CardSuit getSuit() {
		return suit;
	}
	
	public CardFace getFace() {
		return face;
	}
	
	public Long getCount() {
		return count;
	}

	public UUID getGameUuid() {
		return gameUuid;
	}

	@Override
	public int compareTo(SuitFaceSummaryView o) {
		int suitResult = this.suit.getWeight().compareTo(o.suit.getWeight());
		
		if (suitResult != 0) {
			return suitResult;
		}
		
		return this.face.getFaceValue().compareTo(o.face.getFaceValue());
	}
}
