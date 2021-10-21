package com.logmein.cardgames.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.GamesApi;
import com.logmein.cardgames.api.views.SuitFaceSummaryView;
import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;

@Component
public class SuitFacesSummaryViewAssembler implements SimpleRepresentationModelAssembler<SuitFaceSummaryView> {

	@Override
	public void addLinks(EntityModel<SuitFaceSummaryView> resource) {
		UUID gameUuid = resource.getContent().getGameUuid();
		CardSuit suit = resource.getContent().getSuit();
		CardFace face = resource.getContent().getFace();
		resource.add(linkTo(methodOn(GamesApi.class).suitFaceSummary(gameUuid, suit, face)).withSelfRel());
		resource.add(linkTo(methodOn(GamesApi.class).suitsFacesSummaries(gameUuid)).withRel("suitsFaces"));
		resource.add(linkTo(methodOn(GamesApi.class).one(gameUuid)).withRel("game"));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<SuitFaceSummaryView>> resources) {
	}
}
