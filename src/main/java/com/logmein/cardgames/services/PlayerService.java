package com.logmein.cardgames.services;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.api.views.PlayerView;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.repositories.GameRepository;
import com.logmein.cardgames.domain.repositories.PlayerRepository;
import com.logmein.cardgames.domain.repositories.PlayingCardRepository;

@Service
@Transactional
public class PlayerService {

	private PlayerRepository playerRepository;
	private GameRepository gameRepository;
	private PlayingCardRepository playingCardRepository;
	
	public PlayerService(PlayerRepository playerRepository, GameRepository gameRepository, PlayingCardRepository playingCardRepository) {
		this.playerRepository = playerRepository;
		this.gameRepository = gameRepository;
		this.playingCardRepository = playingCardRepository;
	}
	
	public PlayerView addPlayerToGame(AddPlayerCommand command) {
		Game game = gameRepository.findOneByUuid(command.gameUuid).orElseThrow();
		
		Player newPlayer = playerRepository.save(new Player(command.name, game));
		
		PlayerView playerView = new PlayerView(newPlayer.getName(), newPlayer.getUuid(), 0);
		playerView.setGame(new GameView(game.getUuid(), game.getName()));
		return playerView;
	}
	
	public void deletePlayer(UUID uuid) {
		Player player = playerRepository.findOneByUuid(uuid).orElseThrow();
		playerRepository.delete(player);
	}
	
	//TODO improve this method
	public List<PlayerView> getPlayersOfGame(UUID gameUuid) {
		List<Player> players = playerRepository.findAllByGame(gameUuid);
		List<PlayingCard> playingCards = playingCardRepository.findAllOnHandByGame(gameUuid);
		
		var cardsPerPlayer = playingCards.stream()
										.collect(Collectors
										.groupingBy(PlayingCard::getPlayer));
		
		return players.stream()
					.map(p -> new PlayerView(p.getName(), p.getUuid(), 
							calculateHandValue(cardsPerPlayer.get(p)))
						)
					.sorted(Comparator.reverseOrder())
					.collect(Collectors.toList());
	}
	
	private static Integer calculateHandValue(List<PlayingCard> hand) {
		if (hand == null)
			return 0;
		
		return hand.stream()
					.map(pc -> pc.getCard().getFace().getFaceValue())
					.reduce(0, Integer::sum);			
	}

	public PlayerView one(UUID playerUuid) {
		Player player = playerRepository.findOneWithGameByUuid(playerUuid).orElseThrow();
		PlayerView playerView = new PlayerView(player.getName(), player.getUuid(), 0);
		playerView.setGame(new GameView(player.getGame().getUuid(), player.getGame().getName()));
		return playerView;
	}
}
