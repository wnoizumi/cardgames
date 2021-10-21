package com.logmein.cardgames.api.views;

import java.util.UUID;

public class PlayerView implements Comparable<PlayerView> {
	
	private String name;
	private UUID uuid;
	private Integer handValue;
	
	public PlayerView(String name, UUID uuid, Integer handValue) {
		this.name = name;
		this.uuid = uuid;
		this.handValue = handValue;
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

	@Override
	public int compareTo(PlayerView o) {
		return this.getHandValue().compareTo(o.getHandValue());
	}
}
