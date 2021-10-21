package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.GamesApi;
import com.logmein.cardgames.api.PlayersApi;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.api.views.PlayerView;

@Component
public class PlayerViewAssembler implements SimpleRepresentationModelAssembler<PlayerView> {

	@Override
	public void addLinks(EntityModel<PlayerView> resource) {
		UUID uuid = resource.getContent().getUuid();
		UUID gameUuid = resource.getContent().getGame().getUuid();
		resource.add(linkTo(methodOn(PlayersApi.class).onePlayer(uuid)).withSelfRel());
		resource.add(linkTo(methodOn(PlayersApi.class).dealCard(uuid)).withRel("dealCard"));
		resource.add(linkTo(methodOn(PlayersApi.class).cardsOfPlayer(uuid)).withRel("cards"));
		resource.add(linkTo(methodOn(GamesApi.class).one(gameUuid)).withRel("game"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<PlayerView>> resources) {
		Iterator<EntityModel<PlayerView>> iterator = resources.getContent().iterator();
		if (iterator.hasNext()) {
			GameView gameView = iterator.next().getContent().getGame();
			resources.add(linkTo(methodOn(PlayersApi.class).playersOfGame(gameView.getUuid())).withSelfRel());
		}
	}
}
