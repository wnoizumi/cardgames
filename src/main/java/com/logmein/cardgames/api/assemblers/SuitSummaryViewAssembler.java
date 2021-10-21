package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.GamesApi;
import com.logmein.cardgames.api.views.SuitSummaryView;
import com.logmein.cardgames.domain.entities.CardSuit;

@Component
public class SuitSummaryViewAssembler implements SimpleRepresentationModelAssembler<SuitSummaryView> {

	@Override
	public void addLinks(EntityModel<SuitSummaryView> resource) {
		UUID gameUuid = resource.getContent().getGameUuid();
		CardSuit suit = resource.getContent().getSuit();
		resource.add(linkTo(methodOn(GamesApi.class).suitSummary(gameUuid, suit)).withSelfRel());
		resource.add(linkTo(methodOn(GamesApi.class).suitsSummaries(gameUuid)).withRel("suits"));
		resource.add(linkTo(methodOn(GamesApi.class).one(gameUuid)).withRel("game"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<SuitSummaryView>> resources) {
	}
}
