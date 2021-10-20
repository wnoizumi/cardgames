package com.logmein.cardgames.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;

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
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.exceptions.NoPlayingCardsToDealException;
import com.logmein.cardgames.domain.repositories.GameRepository;
import com.logmein.cardgames.domain.repositories.PlayerRepository;
import com.logmein.cardgames.domain.repositories.PlayingCardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CardgamesApplication.class)
@AutoConfigureTestDatabase
@EnableAutoConfiguration
@Profile("test")
public class PlayingCardServiceIntegrationTest {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PlayingCardRepository playingCardRepository;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private DeckService deckService;
	
	@Autowired
	private PlayingCardService playingCardService;
	
	@After
	public void resetDb() {
		playingCardRepository.deleteAll();
		playerRepository.deleteAll();
		gameRepository.deleteAll();
	}
	
	@Test
	@Transactional
	public void whenDealingCard_GivenAvailableCards_Then_ShouldDealTopCardToPlayer() {
		Game game = gameRepository.saveAndFlush(new Game("game 1"));
		Player player = playerRepository.save(new Player("Player 1", game));
		DeckView deckView = deckService.newDeck();
		DeckGameAssociationCommand command = new DeckGameAssociationCommand();
		command.deckUuid = deckView.getUuid();
		command.gameUuid = game.getUuid();
		
		gameService.addDeckToGame(command);
		
		PlayingCardView cardView = playingCardService.dealCardToPlayer(player.getUuid());
		assertThat(cardView, hasProperty("gameUuid", is(game.getUuid())));
		assertThat(cardView, hasProperty("playerUuid", is(player.getUuid())));
		
		Integer availableCards = playingCardRepository.countAvailableCardsByGame(game.getUuid());
		assertThat(availableCards, is(51));
	}
	
	@Test(expected = NoPlayingCardsToDealException.class)
	@Transactional
	public void whenDealingCard_GivenZeroAvailableCards_Then_ShouldThrowException() {
		Game game = gameRepository.saveAndFlush(new Game("game 1"));
		Player player = playerRepository.save(new Player("Player 1", game));
		
		playingCardService.dealCardToPlayer(player.getUuid());
	}
}
