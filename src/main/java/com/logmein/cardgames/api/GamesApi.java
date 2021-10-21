package com.logmein.cardgames.api;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logmein.cardgames.api.assemblers.DeckViewAssembler;
import com.logmein.cardgames.api.assemblers.GameViewAssembler;
import com.logmein.cardgames.api.assemblers.PlayingCardViewAssembler;
import com.logmein.cardgames.api.assemblers.SuitFacesSummaryViewAssembler;
import com.logmein.cardgames.api.assemblers.SuitSummaryViewAssembler;
import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.commands.GamePatchCommand;
import com.logmein.cardgames.api.commands.GamePatchOperation;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.api.views.SuitFaceSummaryView;
import com.logmein.cardgames.api.views.SuitSummaryView;
import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;
import com.logmein.cardgames.services.GameService;
import com.logmein.cardgames.services.PlayingCardService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Games")
@RestController
@RequestMapping(path = "/api/games")
public class GamesApi {
	
	private GameService gameService;
	private PlayingCardService playingCardService;
	private GameViewAssembler gameViewAssembler;
	private DeckViewAssembler deckViewAssembler;
	private PlayingCardViewAssembler playingCardViewAssembler;
	private SuitSummaryViewAssembler suitSummaryViewAssembler;
	private SuitFacesSummaryViewAssembler suitFacesSummaryViewAssembler;
	
	public GamesApi(GameService gameService, PlayingCardService playingCardService,
			GameViewAssembler gameViewAssembler, DeckViewAssembler deckViewAssembler,
			PlayingCardViewAssembler playingCardViewAssembler, 
			SuitSummaryViewAssembler suitSummaryViewAssembler,
			SuitFacesSummaryViewAssembler suitFacesSummaryViewAssembler) {
		this.gameService = gameService;
		this.playingCardService = playingCardService;
		this.gameViewAssembler = gameViewAssembler;
		this.deckViewAssembler = deckViewAssembler;
		this.playingCardViewAssembler = playingCardViewAssembler;
		this.suitSummaryViewAssembler = suitSummaryViewAssembler;
		this.suitFacesSummaryViewAssembler = suitFacesSummaryViewAssembler;
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

	@GetMapping("{uuid}/cards/{carduuid}")
	public EntityModel<PlayingCardView> playingCard(@PathVariable(name = "uuid") UUID uuid,
			@PathVariable(name = "carduuid") UUID cardUuid) {
		return playingCardViewAssembler.toModel(playingCardService.one(cardUuid));
	}
	
	@GetMapping("{uuid}/suits")
	public ResponseEntity<CollectionModel<EntityModel<SuitSummaryView>>> suitsSummaries(@PathVariable(name = "uuid") UUID uuid) {
		List<SuitSummaryView> suitsSummaryOfGame = playingCardService.getSuitsSummaryOfGame(uuid);
		return ResponseEntity.ok(suitSummaryViewAssembler.toCollectionModel(suitsSummaryOfGame));
	}
	
	@GetMapping("{uuid}/suits/{suit}")
	public EntityModel<SuitSummaryView> suitSummary(@PathVariable(name = "uuid") UUID uuid, @PathVariable(name = "suit") CardSuit suit) {
		SuitSummaryView suitSummary = playingCardService.getSuitSummaryOfGame(uuid, suit);
		return suitSummaryViewAssembler.toModel(suitSummary);
	}

	@GetMapping("{uuid}/suitsFaces")
	public ResponseEntity<CollectionModel<EntityModel<SuitFaceSummaryView>>> suitsFacesSummaries(@PathVariable(name = "uuid") UUID uuid) {
		List<SuitFaceSummaryView> suitsSummaryOfGame = playingCardService.getSuitFacesSummariesOfGame(uuid);
		return ResponseEntity.ok(suitFacesSummaryViewAssembler.toCollectionModel(suitsSummaryOfGame));
	}
	
	@GetMapping("{uuid}/suitsFaces/{suit}/{face}")
	public EntityModel<SuitFaceSummaryView> suitFaceSummary(@PathVariable(name = "uuid") UUID uuid, @PathVariable(name = "suit") CardSuit suit, @PathVariable(name = "face") CardFace face) {
		SuitFaceSummaryView suitSummary = playingCardService.getSuitFaceSummaryOfGame(uuid, suit, face);
		return suitFacesSummaryViewAssembler.toModel(suitSummary);
	}
	
	@PatchMapping("{uuid}")
	public ResponseEntity<?> patch(@PathVariable UUID uuid, @Valid GamePatchCommand command) {
		if (command.operation.equals(GamePatchOperation.SHUFFLE)) {
			playingCardService.shuffleCardsOfGame(uuid);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
}
