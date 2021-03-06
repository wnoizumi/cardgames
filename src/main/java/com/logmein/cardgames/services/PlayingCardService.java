package com.logmein.cardgames.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.api.views.SuitFaceSummaryView;
import com.logmein.cardgames.api.views.SuitSummaryView;
import com.logmein.cardgames.domain.entities.Card;
import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.exceptions.NoPlayingCardsToDealException;
import com.logmein.cardgames.domain.projections.SuitFaceProjection;
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
		
		return new PlayingCardView(card.getFace(), card.getSuit(), dealtCard.getUuid(), game.getUuid(), player.getUuid());
	}
	
	public List<PlayingCardView> getCardsOfPlayer(UUID playerUuid) {
		return playingCardRepository.findAllByPlayer(playerUuid)
							.stream()
							.map(pc -> new PlayingCardView(pc.getFace(), pc.getSuit(), pc.getUuid(), pc.getGameUuid(), pc.getPlayerUuid()))
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
							.map(es -> new SuitSummaryView(es.getKey(), es.getValue(), gameUuid))
							.collect(Collectors.toList());
	}
	
	public List<SuitFaceSummaryView> getSuitFacesSummariesOfGame(UUID gameUuid) {
		List<SuitFaceProjection> projections = playingCardRepository.findAllSuitFaceSummariesByGame(gameUuid);
		
		return projections.stream()
							.map(p -> new SuitFaceSummaryView(p.getSuit(), p.getFace(), p.getCount(), gameUuid))
							.sorted(Comparator.reverseOrder())
							.collect(Collectors.toList());
	}
	
	public void shuffleCardsOfGame(UUID gameUuid) {
		List<PlayingCard> cardsToShuffle = playingCardRepository.findAllAvailableByGame(gameUuid);
		List<PlayingCard> shuffledCards = new ArrayList<>();
		Random rand = new Random();
		int currentPosition = 1;
		while (cardsToShuffle.size() > 0) {
			int nextIndex = rand.nextInt(cardsToShuffle.size());
			PlayingCard card = cardsToShuffle.remove(nextIndex);
			card.setShufflePosition(currentPosition);
			shuffledCards.add(card);
			currentPosition++;
		}
		playingCardRepository.saveAll(shuffledCards);
	}

	public PlayingCardView one(UUID uuid) {
		PlayingCard pc = playingCardRepository.findOneWithRelationsByUuid(uuid).orElseThrow();
		Card card = pc.getCard();
		UUID playerUuid = pc.getPlayer() != null ? pc.getPlayer().getUuid() : null;
		return new PlayingCardView(card.getFace(), card.getSuit(), uuid, pc.getGame().getUuid(), playerUuid);
	}

	public SuitSummaryView getSuitSummaryOfGame(UUID uuid, CardSuit suit) {
		List<SuitSummaryView> list = this.getSuitsSummaryOfGame(uuid);
		var result = list.stream()
					.filter(e -> e.getSuit().equals(suit))
					.collect(Collectors.toList());
		
		if (result.size() == 1) {
			return result.get(0);
		}
		
		return new SuitSummaryView(suit, 0L, uuid);
	}

	public SuitFaceSummaryView getSuitFaceSummaryOfGame(UUID uuid, CardSuit suit, CardFace face) {
		List<SuitFaceProjection> projections = playingCardRepository.findAllSuitFaceSummariesByGame(uuid);
		List<SuitFaceProjection> result = projections.stream()
					.filter(e -> e.getSuit().equals(suit))
					.collect(Collectors.toList());
		
		if (result.size() == 1) {
			SuitFaceProjection projection = result.get(0);
			return new SuitFaceSummaryView(projection.getSuit(), projection.getFace(), projection.getCount(), uuid);
		}
		
		return new SuitFaceSummaryView(suit, face, 0L, uuid);
	}
}
