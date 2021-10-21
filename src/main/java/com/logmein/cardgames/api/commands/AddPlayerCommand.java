package com.logmein.cardgames.api.commands;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class AddPlayerCommand {

	@NotNull
	public UUID gameUuid;
	@NotNull
	public String name;
	
	public AddPlayerCommand() {}
	
	public AddPlayerCommand(@NotNull UUID gameUuid, @NotNull String name) {
		this.gameUuid = gameUuid;
		this.name = name;
	}
}
