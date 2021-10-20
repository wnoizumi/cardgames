package com.logmein.cardgames.domain.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private CardFace face;
	
	@Enumerated(EnumType.STRING)
	private CardSuit suit;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Deck deck;
	
	/***
	 * This constructor should only be used by Hibernate
	 */
	@Deprecated
	protected Card() {}
	
	public Card(CardFace face, CardSuit suit, Deck deck) {
		this.face = face;
		this.suit = suit;
		this.deck = deck;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public CardFace getFace() {
		return face;
	}

	public void setFace(CardFace face) {
		this.face = face;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public void setSuit(CardSuit suit) {
		this.suit = suit;
	}

	public Deck getDeck() {
		return deck;
	}

	private void setDeck(Deck deck) {
		this.deck = deck;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deck, face, suit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return Objects.equals(deck, other.deck) && face == other.face && suit == other.suit;
	}

	@Override
	public String toString() {
		return "Card [face=" + face + ", suit=" + suit + ", deck=" + deck + "]";
	}
}
