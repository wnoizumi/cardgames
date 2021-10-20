package com.logmein.cardgames.api.commands;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class AddPlayerCommand {

	@NotNull
	public UUID gameUuid;
	@NotNull
	public String name;
}
