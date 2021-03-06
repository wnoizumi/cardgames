package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.GamesApi;
import com.logmein.cardgames.api.views.GameView;

@Component
public class GameViewAssembler implements SimpleRepresentationModelAssembler<GameView> {

	@Override
	public void addLinks(EntityModel<GameView> resource) {
		UUID uuid = resource.getContent().getUuid();
		resource.add(linkTo(methodOn(GamesApi.class).one(uuid)).withSelfRel());
		resource.add(linkTo(methodOn(GamesApi.class).all()).withRel("games"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<GameView>> resources) {
		resources.add(linkTo(methodOn(GamesApi.class).all()).withSelfRel());
	}

}
