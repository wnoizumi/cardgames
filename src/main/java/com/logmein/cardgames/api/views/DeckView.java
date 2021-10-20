package com.logmein.cardgames.api.views;

import java.util.UUID;

public class DeckView {
	
	private UUID uuid;
	private GameView gameView;
	
	public DeckView(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public GameView getGameView() {
		return gameView;
	}
}
