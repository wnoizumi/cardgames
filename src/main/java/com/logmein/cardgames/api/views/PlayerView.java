package com.logmein.cardgames.api.views;

import java.util.UUID;

public class PlayerView {
	
	private String name;
	private UUID uuid;
	
	public PlayerView(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}
}
