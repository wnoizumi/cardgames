package com.logmein.cardgames.api.views;

import java.util.UUID;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "decks")
public class DeckView {
	
	private UUID uuid;
	private GameView game;
	
	public DeckView(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public GameView getGame() {
		return game;
	}

	public void setGame(GameView gameView) {
		this.game = gameView;
	}
}
