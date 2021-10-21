package com.logmein.cardgames.api.views;

import java.util.UUID;

public class PlayerView implements Comparable<PlayerView> {
	
	private String name;
	private UUID uuid;
	private Integer handValue;
	private GameView game;
	
	public PlayerView(String name, UUID uuid, Integer handValue) {
		this.name = name;
		this.uuid = uuid;
		this.handValue = handValue;
	}
	
	public PlayerView(String name, UUID uuid, Integer handValue, GameView game) {
		this.name = name;
		this.uuid = uuid;
		this.handValue = handValue;
		this.game = game;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Integer getHandValue() {
		return handValue;
	}

	public GameView getGame() {
		return game;
	}

	public void setGame(GameView game) {
		this.game = game;
	}

	@Override
	public int compareTo(PlayerView o) {
		return this.getHandValue().compareTo(o.getHandValue());
	}
}
