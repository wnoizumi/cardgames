package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.DecksApi;
import com.logmein.cardgames.api.views.DeckView;

@Component
public class DeckViewAssembler implements SimpleRepresentationModelAssembler<DeckView> {

	@Override
	public void addLinks(EntityModel<DeckView> resource) {
		UUID uuid = resource.getContent().getUuid();
		resource.add(linkTo(methodOn(DecksApi.class).one(uuid)).withSelfRel());
//		resource.add(linkTo(methodOn(DecksApi.class).delete(uuid)).withRel("delete"));
		resource.add(linkTo(methodOn(DecksApi.class).all()).withRel("decks"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<DeckView>> resources) {
		resources.add(linkTo(methodOn(DecksApi.class).all()).withSelfRel());
	}
}
