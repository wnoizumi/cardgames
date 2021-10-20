package com.logmein.cardgames.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.views.PlayerView;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.repositories.GameRepository;
import com.logmein.cardgames.domain.repositories.PlayerRepository;

@Service
@Transactional
public class PlayerService {

	private PlayerRepository playerRepository;
	private GameRepository gameRepository;
	
	public PlayerService(PlayerRepository playerRepository, GameRepository gameRepository) {
		this.playerRepository = playerRepository;
		this.gameRepository = gameRepository;
	}
	
	public PlayerView addPlayerToGame(AddPlayerCommand command) {
		Game game = gameRepository.findOneByUuid(command.gameUuid).orElseThrow();
		
		Player newPlayer = playerRepository.save(new Player(command.name, game));
		
		return new PlayerView(newPlayer.getName(), newPlayer.getUuid());
	}
}
