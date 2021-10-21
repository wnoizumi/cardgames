package com.logmein.cardgames.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.views.SuitSummaryView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.domain.entities.Card;
import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.exceptions.NoPlayingCardsToDealException;
import com.logmein.cardgames.domain.repositories.PlayerRepository;
import com.logmein.cardgames.domain.repositories.PlayingCardRepository;

@Service
@Transactional
public class PlayingCardService {

	private PlayingCardRepository playingCardRepository;
	private PlayerRepository playerRepository;
	
	public PlayingCardService(PlayingCardRepository playingCardRepository, PlayerRepository playerRepository) {
		this.playingCardRepository = playingCardRepository;
		this.playerRepository = playerRepository;
	}
	
	public PlayingCardView dealCardToPlayer(UUID playerUuid) {
		Player player = playerRepository.findOneWithGameByUuid(playerUuid).orElseThrow();
		Game game = player.getGame();
		List<PlayingCard> playingCards = playingCardRepository.findNextDealByGame(game.getUuid(), PageRequest.of(0, 1));
		
		if (playingCards.size() != 1)
			throw new NoPlayingCardsToDealException("There is no card in the game board to deal.");
		
		PlayingCard dealtCard = playingCards.get(0);
		
		dealtCard.setPlayer(player);
		dealtCard.setShufflePosition(null);
		
		dealtCard = playingCardRepository.save(dealtCard);
		Card card = dealtCard.getCard();
		
		return new PlayingCardView(card.getFace(), card.getSuit(), game.getUuid(), player.getUuid());
	}
	
	public List<PlayingCardView> getCardsOfPlayer(UUID playerUuid) {
		return playingCardRepository.findAllByPlayer(playerUuid)
							.stream()
							.map(pc -> new PlayingCardView(pc.getFace(), pc.getSuit(), pc.getGameUuid(), pc.getPlayerUuid()))
							.collect(Collectors.toList());
	}
	
	public List<SuitSummaryView> getSuitsSummaryOfGame(UUID gameUuid) {
		//TODO perform grouping in the db and return a projection
		List<PlayingCard> cards = playingCardRepository.findAllAvailableByGame(gameUuid);
		var countPerFace = cards.stream()
							.map(c -> c.getCard())
							.collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));
		
		return countPerFace.entrySet()
							.stream()
							.map(es -> new SuitSummaryView(es.getKey(), es.getValue()))
							.collect(Collectors.toList());
	}
}
