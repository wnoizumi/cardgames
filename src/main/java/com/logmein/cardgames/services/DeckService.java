package com.logmein.cardgames.services;

import org.springframework.stereotype.Service;

import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.domain.builders.DeckBuilder;
import com.logmein.cardgames.domain.entities.Deck;
import com.logmein.cardgames.domain.repositories.DeckRepository;

@Service
public class DeckService {

	private DeckRepository deckRepository;
	private DeckBuilder deckBuilder;
	
	public DeckService(DeckRepository deckRepository, DeckBuilder deckBuilder) {
		this.deckRepository = deckRepository;
		this.deckBuilder = deckBuilder;
	}
	
	public DeckView newDeck() {
		Deck deck = deckRepository.save(deckBuilder.buildDeck());

		return new DeckView(deck.getUuid());
	}
}
