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

import com.logmein.cardgames.api.assemblers.GameViewAssembler;
import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.services.GameService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Games")
@RestController
@RequestMapping(path = "/gamesboard/api/games")
public class GamesApi {
	
	private GameService gameService;
	private GameViewAssembler gameViewAssembler;
	
	public GamesApi(GameService gameService, GameViewAssembler gameViewAssembler) {
		this.gameService = gameService;
		this.gameViewAssembler = gameViewAssembler;
	}

	@PostMapping
	public ResponseEntity<?> newGame(@RequestBody CreateGameCommand createGameCommand) {
		EntityModel<GameView> entityModel = gameViewAssembler.toModel(gameService.newGame(createGameCommand));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<GameView>>> all() {
		CollectionModel<EntityModel<GameView>> games = gameViewAssembler.toCollectionModel(gameService.all());
		return ResponseEntity.ok(games);
	}

	@DeleteMapping("{uuid}")
	public ResponseEntity<?> delete(@PathVariable UUID uuid) {
		gameService.deleteGame(uuid);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("{uuid}")
	public EntityModel<GameView> one(@PathVariable UUID uuid) {
		return gameViewAssembler.toModel(gameService.one(uuid));
	}
//
//	@PostMapping("{uuid}/decks")
//	public ResponseEntity<?> addDeck(@PathVariable UUID uuid,
//			@RequestBody DeckGameAssociationCommand deckGameAssociationCommand) {
//		EntityModel<GameView> entityModel = null;// assembler.toModel(repository.save(newEmployee));
//		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
//	}
//
//	@PostMapping("{uuid}/players")
//	public ResponseEntity<?> addPlayer(@PathVariable UUID uuid, @RequestBody AddPlayerCommand newPlayerCommand) {
//		EntityModel<GameView> entityModel = null;// assembler.toModel(repository.save(newEmployee));
//		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
//	}
//
//	@DeleteMapping("{uuid}/players/{playeruuid}")
//	public ResponseEntity<?> removePlayer(@PathVariable(name = "uuid") UUID uuid,
//			@PathVariable(name = "playeruuid") UUID playerUuid) {
//		// repository.deleteById(id);
//		return ResponseEntity.ok().build();
//	}
//	
//	@PostMapping("{uuid}/players/{playeruuid}/cards")
//	public ResponseEntity<?> dealCard(@PathVariable(name = "uuid") UUID uuid,
//			@PathVariable(name = "playeruuid") UUID playerUuid) {
//		EntityModel<GameView> entityModel = null;// assembler.toModel(repository.save(newEmployee));
//		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
//	}
//	
//	@GetMapping("{uuid}/players/{playeruuid}/cards")
//	public ResponseEntity<CollectionModel<EntityModel<CardView>>> cardsOfPlayer(@PathVariable(name = "uuid") UUID uuid,
//			@PathVariable(name = "playeruuid") UUID playerUuid) {
//		return null;
//	}
}
