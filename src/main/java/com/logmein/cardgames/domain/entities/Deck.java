package com.logmein.cardgames.domain.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.logmein.cardgames.domain.exceptions.InvalidDeckOperation;

@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class Deck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private UUID uuid;
	
	@OneToMany(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true, 
			mappedBy = "deck")
	private Set<Card> cards = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;
	
	public Deck() {
		this.uuid = UUID.randomUUID();
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	private void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public List<Card> getCards() {
		//We return a new list to avoid changes in our set of cards
		return new ArrayList<>(this.cards);
	}

	public void setCards(Set<Card> cards) {
		if (this.cards != null && this.cards.size() > 0)
			throw new InvalidDeckOperation("The cards of a deck should not be changed");
		this.cards = cards;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deck other = (Deck) obj;
		return Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return "Deck [uuid=" + uuid + "]";
	}
	
}
