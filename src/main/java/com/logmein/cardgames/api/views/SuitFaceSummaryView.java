package com.logmein.cardgames.api.views;

import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;

public class SuitFaceSummaryView implements Comparable<SuitFaceSummaryView> {

	private CardSuit suit;
	private CardFace face;
	private Long count;

	public SuitFaceSummaryView(CardSuit suit, CardFace face, Long count) {
		this.suit = suit;
		this.face = face;
		this.count = count;
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

	@Override
	public int compareTo(SuitFaceSummaryView o) {
		int suitResult = this.suit.getWeight().compareTo(o.suit.getWeight());
		
		if (suitResult != 0) {
			return suitResult;
		}
		
		return this.face.getFaceValue().compareTo(o.face.getFaceValue());
	}
}
