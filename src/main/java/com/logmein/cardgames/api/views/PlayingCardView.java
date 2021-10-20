package com.logmein.cardgames.api.views;

import java.util.UUID;

import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;

public class PlayingCardView {
	
	private CardFace face;
	private CardSuit suit;
	private UUID gameUuid;
	private UUID playerUuid;
	
	public PlayingCardView(CardFace face, CardSuit suit, UUID gameUuid, UUID playerUuid) {
		this.face = face;
		this.suit = suit;
		this.gameUuid = gameUuid;
		this.playerUuid = playerUuid;
	}

	public CardFace getFace() {
		return face;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public UUID getGameUuid() {
		return gameUuid;
	}

	public UUID getPlayerUuid() {
		return playerUuid;
	}
}
