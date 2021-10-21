package com.logmein.cardgames.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.domain.entities.Card;
import com.logmein.cardgames.domain.entities.Deck;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.repositories.DeckRepository;
import com.logmein.cardgames.domain.repositories.GameRepository;
import com.logmein.cardgames.domain.repositories.PlayingCardRepository;

@Service
@Transactional
public class GameService {

	private GameRepository gameRepository;
	private DeckRepository deckRepository;
	private PlayingCardRepository playingCardRepository;

	public GameService(GameRepository gameRepository, DeckRepository deckRepository, PlayingCardRepository playingCardRepository) {
		this.gameRepository = gameRepository;
		this.deckRepository = deckRepository;
		this.playingCardRepository = playingCardRepository;
	}

	public GameView newGame(CreateGameCommand command) {
		Game game = gameRepository.save(new Game(command.name));

		return new GameView(game.getUuid(), game.getName());
	}
	
	public void deleteGame(UUID uuid) {
		Game game = gameRepository.findOneByUuid(uuid).orElseThrow();
		gameRepository.delete(game);
	}
	
	public GameView addDeckToGame(DeckGameAssociationCommand command) {
		Deck deck = deckRepository.findOneWithRelationsByUuid(command.deckUuid).orElseThrow();
		Game game = gameRepository.findOneByUuid(command.gameUuid).orElseThrow();
		int availableCards = playingCardRepository.countAvailableCardsByGame(command.gameUuid);
		
		deck.setGame(game);
		
		List<PlayingCard> playingCards = new ArrayList<>();
		int nextShufflePosition = availableCards + 1;
		for (Card card : deck.getCards()) {
			PlayingCard playingCard = new PlayingCard(card, game);
			
			playingCard.setShufflePosition(nextShufflePosition);
			nextShufflePosition++;
			
			playingCards.add(playingCard);
		}
		
		deckRepository.save(deck);
		playingCardRepository.saveAll(playingCards);
		
		return new GameView(game.getUuid(), game.getName());
	}

	public List<GameView> all() {
		return gameRepository.findAll()
							.stream()
							.map(g -> new GameView(g.getUuid(), g.getName()))
							.collect(Collectors.toList());
	}

	public GameView one(UUID uuid) {
		Game game = gameRepository.findOneByUuid(uuid).orElseThrow();
		return new GameView(game.getUuid(), game.getName()); 
	}
}
