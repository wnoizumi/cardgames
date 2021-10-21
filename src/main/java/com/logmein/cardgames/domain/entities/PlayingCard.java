package com.logmein.cardgames.domain.entities;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class PlayingCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private UUID uuid;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private Card card;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Game game;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;
	
	private Integer shufflePosition;
	
	protected PlayingCard() {
		this.uuid = UUID.randomUUID();
	}
	
	public PlayingCard(Card card, Game game) {
		this();
		this.card = card;
		this.game = game;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	private void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Card getCard() {
		return card;
	}

	private void setCard(Card card) {
		this.card = card;
	}

	public Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getShufflePosition() {
		return shufflePosition;
	}

	public void setShufflePosition(Integer shufflePosition) {
		this.shufflePosition = shufflePosition;
	}

	@Override
	public int hashCode() {
		return Objects.hash(card, game);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayingCard other = (PlayingCard) obj;
		return Objects.equals(card, other.card) && Objects.equals(game, other.game);
	}

	@Override
	public String toString() {
		return "PlayingCard [card=" + card + ", game=" + game + "]";
	}
}
