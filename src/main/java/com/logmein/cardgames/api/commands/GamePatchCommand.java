package com.logmein.cardgames.api.commands;

import javax.validation.constraints.NotNull;

public class GamePatchCommand {
	
	@NotNull
	public GamePatchOperation operation;
}
