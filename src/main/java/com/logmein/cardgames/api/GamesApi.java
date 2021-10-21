package com.logmein.cardgames.api;

import java.util.UUID;

import javax.validation.Valid;

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
import com.logmein.cardgames.api.assemblers.GameViewAssembler;
import com.logmein.cardgames.api.assemblers.PlayerViewAssembler;
import com.logmein.cardgames.api.assemblers.PlayingCardViewAssembler;
import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.api.views.PlayerView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.services.GameService;
import com.logmein.cardgames.services.PlayerService;
import com.logmein.cardgames.services.PlayingCardService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Games")
@RestController
@RequestMapping(path = "/api/games")
public class GamesApi {
	
	private GameService gameService;
	private PlayerService playerService;
	private PlayingCardService playingCardService;
	private GameViewAssembler gameViewAssembler;
	private DeckViewAssembler deckViewAssembler;
	private PlayerViewAssembler playerViewAssembler;
	private PlayingCardViewAssembler playingCardViewAssembler;
	
	public GamesApi(GameService gameService, PlayerService playerService, PlayingCardService playingCardService,
			GameViewAssembler gameViewAssembler, DeckViewAssembler deckViewAssembler,
			PlayerViewAssembler playerViewAssembler, PlayingCardViewAssembler playingCardViewAssembler) {
		this.gameService = gameService;
		this.playerService = playerService;
		this.playingCardService = playingCardService;
		this.gameViewAssembler = gameViewAssembler;
		this.deckViewAssembler = deckViewAssembler;
		this.playerViewAssembler = playerViewAssembler;
		this.playingCardViewAssembler = playingCardViewAssembler;
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

	@PostMapping("{uuid}/decks")
	public ResponseEntity<?> addDeck(@PathVariable UUID uuid,
			@RequestBody @Valid DeckGameAssociationCommand deckGameAssociationCommand) {
		DeckView deckView = gameService.addDeckToGame(uuid, deckGameAssociationCommand);
		EntityModel<DeckView> entityModel = deckViewAssembler.toModel(deckView);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@PostMapping("{uuid}/players")
	public ResponseEntity<?> addPlayer(@PathVariable UUID uuid, @RequestBody @Valid AddPlayerCommand addPlayerCommand) {
		EntityModel<PlayerView> entityModel = playerViewAssembler.toModel(playerService.addPlayerToGame(addPlayerCommand));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PostMapping("{uuid}/players/{playeruuid}")
	public EntityModel<PlayerView> onePlayer(@PathVariable(name = "uuid") UUID uuid,
			@PathVariable(name = "playeruuid") UUID playerUuid) {
		
		return playerViewAssembler.toModel(playerService.one(playerUuid));
	}

	@DeleteMapping("{uuid}/players/{playeruuid}")
	public ResponseEntity<?> removePlayer(@PathVariable(name = "uuid") UUID uuid,
			@PathVariable(name = "playeruuid") UUID playerUuid) {
		 playerService.deletePlayer(playerUuid);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("{uuid}/players/{playeruuid}/cards")
	public ResponseEntity<?> dealCard(@PathVariable(name = "uuid") UUID uuid,
			@PathVariable(name = "playeruuid") UUID playerUuid) {
		PlayingCardView cardView = playingCardService.dealCardToPlayer(playerUuid);
		EntityModel<PlayingCardView> entityModel = playingCardViewAssembler.toModel(cardView);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@GetMapping("{uuid}/players/{playeruuid}/cards")
	public ResponseEntity<CollectionModel<EntityModel<PlayingCardView>>> cardsOfPlayer(@PathVariable(name = "uuid") UUID uuid,
			@PathVariable(name = "playeruuid") UUID playerUuid) {
		CollectionModel<EntityModel<PlayingCardView>> collectionModel = playingCardViewAssembler.toCollectionModel(playingCardService.getCardsOfPlayer(playerUuid));
		return ResponseEntity.ok(collectionModel);
	}
	
	@GetMapping("{uuid}/cards/{carduuid}")
	public EntityModel<PlayingCardView> playingCard(@PathVariable(name = "uuid") UUID uuid,
			@PathVariable(name = "carduuid") UUID cardUuid) {
		return playingCardViewAssembler.toModel(playingCardService.one(cardUuid));
	}
}
