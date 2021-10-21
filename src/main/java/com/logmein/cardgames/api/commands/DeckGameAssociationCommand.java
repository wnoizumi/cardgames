package com.logmein.cardgames.api.commands;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class DeckGameAssociationCommand {

	@NotNull
	public UUID deckUuid;

	public DeckGameAssociationCommand() {}
	
	public DeckGameAssociationCommand(@NotNull UUID deckUuid) {
		this.deckUuid = deckUuid;
	}
}
