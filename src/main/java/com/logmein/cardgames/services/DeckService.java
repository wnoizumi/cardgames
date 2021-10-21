package com.logmein.cardgames.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.domain.builders.DeckBuilder;
import com.logmein.cardgames.domain.entities.Deck;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.exceptions.CannotDeleteDeckOfAGameException;
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

	public List<DeckView> all() {
		return deckRepository.findAll()
				.stream()
				.map(d -> new DeckView(d.getUuid()))
				.collect(Collectors.toList());
	}

	public void deleteDeck(UUID uuid) {
		Deck deck = deckRepository.findOneByWithGameUuid(uuid).orElseThrow();
		if (deck.getGame() != null) {
			throw new CannotDeleteDeckOfAGameException();
		}
		deckRepository.delete(deck);
	}

	public DeckView one(UUID uuid) {
		Deck deck = deckRepository.findOneByWithGameUuid(uuid).orElseThrow();
		DeckView deckView = new DeckView(deck.getUuid());
		if (deck.getGame() != null) {
			deckView.setGame(new GameView(deck.getGame().getUuid(), deck.getGame().getName()));
		}
		return deckView;
	}
}
