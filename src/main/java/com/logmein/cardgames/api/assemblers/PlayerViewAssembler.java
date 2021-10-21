package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.GamesApi;
import com.logmein.cardgames.api.views.PlayerView;

@Component
public class PlayerViewAssembler implements SimpleRepresentationModelAssembler<PlayerView> {

	@Override
	public void addLinks(EntityModel<PlayerView> resource) {
		UUID uuid = resource.getContent().getUuid();
		UUID gameUuid = resource.getContent().getGame().getUuid();
		resource.add(linkTo(methodOn(GamesApi.class).onePlayer(gameUuid, uuid)).withSelfRel());
		resource.add(linkTo(methodOn(GamesApi.class).one(gameUuid)).withRel("game"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<PlayerView>> resources) {
		resources.add(linkTo(methodOn(GamesApi.class).all()).withSelfRel());
	}

}
