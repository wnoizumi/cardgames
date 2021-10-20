package com.logmein.cardgames.domain.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private UUID uuid;
	
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, 
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			mappedBy = "game") 
	private Set<Player> players;
	
	@OneToMany(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			mappedBy = "game")
	private Set<PlayingCard> playingCards;
	
	@OneToMany(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy = "game")
	private List<Deck> decks;
	
	public Game() {
		this.uuid = UUID.randomUUID();
	}
	
	public Game(String name) {
		this();
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
	public Set<PlayingCard> getPlayingCards() {
		return playingCards;
	}

	private void setPlayingCards(Set<PlayingCard> playingCards) {
		this.playingCards = playingCards;
	}
	
	public List<Deck> getDecks() {
		return decks;
	}

	public void setDecks(List<Deck> decks) {
		this.decks = decks;
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
		Game other = (Game) obj;
		return Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return "Game [uuid=" + uuid + ", name=" + name + "]";
	}

	public void shuffleCards() {
	}
}
