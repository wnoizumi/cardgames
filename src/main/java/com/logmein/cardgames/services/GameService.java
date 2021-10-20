package com.logmein.cardgames.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.repositories.GameRepository;

@Service
public class GameService {

	private GameRepository gameRepository;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public GameView newGame(CreateGameCommand command) {
		Game game = gameRepository.save(new Game(command.name));

		return new GameView(game.getUuid(), game.getName());
	}
	
	@Transactional
	public void deleteGame(UUID uuid) {
		Game game = gameRepository.findOneByUuid(uuid).orElseThrow();
		gameRepository.delete(game);
	}
}
