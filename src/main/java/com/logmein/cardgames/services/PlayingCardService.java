package com.logmein.cardgames.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.domain.entities.Card;
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
}
