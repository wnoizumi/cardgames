package com.logmein.cardgames.api.views;

import java.util.UUID;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "games")
public class GameView {
	
	private UUID uuid;
	private String name;
	
	public GameView() {}
	
	public GameView(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
