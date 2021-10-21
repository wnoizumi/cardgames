package com.logmein.cardgames;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.services.DeckService;
import com.logmein.cardgames.services.GameService;
import com.logmein.cardgames.services.PlayerService;

@Component
@Profile("dev")
public class DatabaseInitializer implements CommandLineRunner {

	private DeckService deckService;
	private GameService gameService;
	private PlayerService playerService;
	
	public DatabaseInitializer(DeckService deckService, GameService gameService, PlayerService playerService) {
		this.deckService = deckService;
		this.gameService = gameService;
		this.playerService = playerService;
	}

	@Override
	public void run(String... args) throws Exception {
		DeckView deckOne = deckService.newDeck();
		DeckView deckTwo = deckService.newDeck();
		DeckView deckThree = deckService.newDeck();
		
		GameView gameOne = gameService.newGame(new CreateGameCommand("Game 1"));
		GameView gameTwo = gameService.newGame(new CreateGameCommand("Game 2"));
		GameView gameThree = gameService.newGame(new CreateGameCommand("Game 2"));
		
		gameService.addDeckToGame(gameOne.getUuid(), new DeckGameAssociationCommand(deckOne.getUuid()));
		gameService.addDeckToGame(gameOne.getUuid(), new DeckGameAssociationCommand(deckTwo.getUuid()));
		gameService.addDeckToGame(gameTwo.getUuid(), new DeckGameAssociationCommand(deckThree.getUuid()));
		
		playerService.addPlayerToGame(new AddPlayerCommand(gameOne.getUuid(), "Player 1"));
		playerService.addPlayerToGame(new AddPlayerCommand(gameOne.getUuid(), "Player 2"));
		playerService.addPlayerToGame(new AddPlayerCommand(gameThree.getUuid(), "Player 3"));
	}
}
