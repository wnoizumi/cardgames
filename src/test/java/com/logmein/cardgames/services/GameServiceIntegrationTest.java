package com.logmein.cardgames.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.logmein.cardgames.CardgamesApplication;
import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.repositories.GameRepository;
import com.logmein.cardgames.domain.repositories.PlayingCardRepository;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CardgamesApplication.class)
@AutoConfigureTestDatabase
@EnableAutoConfiguration
@Profile("test")
public class GameServiceIntegrationTest {

	@Autowired
	private GameService gameService;
	
	@Autowired
	private DeckService deckService;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private PlayingCardRepository playingCardRepository;
	
	@After
	public void resetDb() {
		gameRepository.deleteAll();
	}
	
	@Test
	public void whenNewGame_WithoutName_Then_ShouldCreateGame() {
		CreateGameCommand command = new CreateGameCommand();
		command.name = null;
		
		GameView gameView = gameService.newGame(command);
		
		assertThat(gameView.getName(), nullValue());
		assertThat(gameView, hasProperty("uuid"));
	}
	
	@Test
	public void whenNewGame_WithName_Then_ShouldSaveToDb() {
		CreateGameCommand command = new CreateGameCommand();
		command.name = "game 1";
		
		GameView gameView = gameService.newGame(command);
		
		assertThat(gameView, hasProperty("name", is("game 1")));
		assertThat(gameView, hasProperty("uuid"));
	}
	
	@Test
	public void whenDeleteExistingGame_Then_ShouldRemoveFromDb() {
		Game existingGame = gameRepository.save(new Game("game 1"));
		
		List<Game> all = gameRepository.findAll();
		
		assertThat(all, hasSize(1));
		
		gameService.deleteGame(existingGame.getUuid());
		
		all = gameRepository.findAll();
		
		assertThat(all, hasSize(0));
	}
	
	@Test
	@Transactional
	public void whenAddingDeckToGame_Then_ShouldCreatePlayingCards() {
		Game game = gameRepository.saveAndFlush(new Game("game 1"));
		DeckView deckView = deckService.newDeck();
		DeckGameAssociationCommand command = new DeckGameAssociationCommand();
		command.deckUuid = deckView.getUuid();
		
		deckView = gameService.addDeckToGame(game.getUuid(), command);
		
		List<PlayingCard> playingCards = playingCardRepository.findAllAvailableByGame(deckView.getGame().getUuid());
		
		assertThat(playingCards, hasSize(52));
	}
}
