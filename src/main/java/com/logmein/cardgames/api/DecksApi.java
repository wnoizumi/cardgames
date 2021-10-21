package com.logmein.cardgames.api;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logmein.cardgames.api.assemblers.DeckViewAssembler;
import com.logmein.cardgames.api.commands.CreateDeckCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.services.DeckService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Decks")
@RestController
@RequestMapping(path = "/api/decks")
public class DecksApi {
	
	private DeckService deckService;
	private DeckViewAssembler deckViewAssembler;

	public DecksApi(DeckService deckService, DeckViewAssembler deckViewAssembler) {
		super();
		this.deckService = deckService;
		this.deckViewAssembler = deckViewAssembler;
	}
	
	@PostMapping
	public ResponseEntity<?> newDeck(@RequestBody CreateDeckCommand createDeckCommand) {
		EntityModel<DeckView> entityModel = deckViewAssembler.toModel(deckService.newDeck());
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<DeckView>>> all() {
		CollectionModel<EntityModel<DeckView>> decks = deckViewAssembler.toCollectionModel(deckService.all());
		return ResponseEntity.ok(decks);
	}

	@DeleteMapping("{uuid}")
	public ResponseEntity<?> delete(@PathVariable UUID uuid) {
		deckService.deleteDeck(uuid);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("{uuid}")
	public EntityModel<DeckView> one(@PathVariable UUID uuid) {
		return deckViewAssembler.toModel(deckService.one(uuid));
	}
}
