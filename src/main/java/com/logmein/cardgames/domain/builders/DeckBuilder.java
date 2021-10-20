package com.logmein.cardgames.domain.builders;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.logmein.cardgames.domain.entities.Card;
import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;
import com.logmein.cardgames.domain.entities.Deck;

@Component
public class DeckBuilder {
	
	public Deck buildDeck() {
		Deck deck = new Deck();
		deck.setCards(createCardsForDeck(deck));
		return deck;
	}
	
	private Set<Card> createCardsForDeck(Deck deck) {
		Set<Card> cards = new HashSet<>();
		for (CardSuit suit : CardSuit.values()) {
			for (CardFace face : CardFace.values()) {
				cards.add(new Card(face, suit, deck));
			}
		}
		
		return cards;
	}

}
