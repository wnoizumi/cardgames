package com.logmein.cardgames.domain.projections;

import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;

public class SuitFaceProjection {
	
	private CardSuit suit;
	private CardFace face;
	private long count;
	
	public SuitFaceProjection(CardFace face, CardSuit suit, long count) {
		super();
		this.face = face;
		this.suit = suit;
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
}
