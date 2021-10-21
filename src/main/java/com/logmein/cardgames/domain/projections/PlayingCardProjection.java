package com.logmein.cardgames.domain.projections;

import java.util.UUID;

import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;

public class PlayingCardProjection {
	
	private CardFace face;
	private CardSuit suit;
	private UUID uuid;
	private UUID gameUuid;
	private UUID playerUuid;
	
	public PlayingCardProjection(CardFace face, CardSuit suit, UUID uuid, UUID gameUuid, UUID playerUuid) {
		this.face = face;
		this.suit = suit;
		this.uuid = uuid;
		this.gameUuid = gameUuid;
		this.playerUuid = playerUuid;
	}

	public CardFace getFace() {
		return face;
	}

	public CardSuit getSuit() {
		return suit;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public UUID getGameUuid() {
		return gameUuid;
	}

	public UUID getPlayerUuid() {
		return playerUuid;
	}
}
