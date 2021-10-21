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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logmein.cardgames.api.assemblers.PlayerViewAssembler;
import com.logmein.cardgames.api.assemblers.PlayingCardViewAssembler;
import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.views.PlayerView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.services.PlayerService;
import com.logmein.cardgames.services.PlayingCardService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Players")
@RestController
@RequestMapping(path = "/api/players")
public class PlayersApi {
	
	private PlayerService playerService;
	private PlayerViewAssembler playerViewAssembler;
	private PlayingCardService playingCardService;
	private PlayingCardViewAssembler playingCardViewAssembler;

	public PlayersApi(PlayerService playerService, PlayerViewAssembler playerViewAssembler,
			PlayingCardService playingCardService, PlayingCardViewAssembler playingCardViewAssembler) {
		this.playerService = playerService;
		this.playerViewAssembler = playerViewAssembler;
		this.playingCardService = playingCardService;
		this.playingCardViewAssembler = playingCardViewAssembler;
	}

	@PostMapping
	public ResponseEntity<?> addPlayer(@RequestBody @Valid AddPlayerCommand addPlayerCommand) {
		EntityModel<PlayerView> entityModel = playerViewAssembler.toModel(playerService.addPlayerToGame(addPlayerCommand));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<PlayerView>>> playersOfGame(@RequestParam(required = true, name = "game") UUID gameUuid) {
		CollectionModel<EntityModel<PlayerView>> games = playerViewAssembler.toCollectionModel(playerService.getPlayersOfGame(gameUuid));
		return ResponseEntity.ok(games);
	}
	
	@PostMapping("{playeruuid}")
	public EntityModel<PlayerView> onePlayer(@PathVariable(name = "playeruuid") UUID playerUuid) {
		
		return playerViewAssembler.toModel(playerService.one(playerUuid));
	}

	@DeleteMapping("{playeruuid}")
	public ResponseEntity<?> removePlayer(@PathVariable(name = "playeruuid") UUID playerUuid) {
		 playerService.deletePlayer(playerUuid);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("{playeruuid}/cards")
	public ResponseEntity<?> dealCard(@PathVariable(name = "playeruuid") UUID playerUuid) {
		PlayingCardView cardView = playingCardService.dealCardToPlayer(playerUuid);
		EntityModel<PlayingCardView> entityModel = playingCardViewAssembler.toModel(cardView);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@GetMapping("{playeruuid}/cards")
	public ResponseEntity<CollectionModel<EntityModel<PlayingCardView>>> cardsOfPlayer(@PathVariable(name = "playeruuid") UUID playerUuid) {
		CollectionModel<EntityModel<PlayingCardView>> collectionModel = playingCardViewAssembler.toCollectionModel(playingCardService.getCardsOfPlayer(playerUuid));
		return ResponseEntity.ok(collectionModel);
	}
}
