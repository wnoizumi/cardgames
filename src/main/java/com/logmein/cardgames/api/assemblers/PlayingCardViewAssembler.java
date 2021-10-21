package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.GamesApi;
import com.logmein.cardgames.api.views.PlayingCardView;

@Component
public class PlayingCardViewAssembler implements SimpleRepresentationModelAssembler<PlayingCardView> {

	@Override
	public void addLinks(EntityModel<PlayingCardView> resource) {
		UUID gameUuid = resource.getContent().getGameUuid();
		resource.add(linkTo(methodOn(GamesApi.class).one(gameUuid)).withRel("game"));
		resource.add(linkTo(methodOn(GamesApi.class).playingCard(gameUuid, resource.getContent().getUuid())).withSelfRel());
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<PlayingCardView>> resources) {
	}

}
