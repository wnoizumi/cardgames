package com.logmein.cardgames.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.SuitSummaryView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.api.views.SuitFaceSummaryView;
import com.logmein.cardgames.domain.entities.CardFace;
import com.logmein.cardgames.domain.entities.CardSuit;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
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

	private Game game;

	private Player player;
	
	@After
	public void resetDb() {
		playingCardRepository.deleteAll();
		playerRepository.deleteAll();
		gameRepository.deleteAll();
	}
	
	private void createGameWithPlayer() {
		game = gameRepository.saveAndFlush(new Game("game 1"));
		player = playerRepository.save(new Player("Player 1", game));
	}

	private void addDeckToGame() {
		DeckView deckView = deckService.newDeck();
		DeckGameAssociationCommand command = new DeckGameAssociationCommand();
		command.deckUuid = deckView.getUuid();
		command.gameUuid = game.getUuid();
		
		gameService.addDeckToGame(command);
	}
	
	@Test
	@Transactional
	public void whenDealingCard_GivenAvailableCards_Then_ShouldDealTopCardToPlayer() {
		createGameWithPlayer();
		addDeckToGame();
		
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
	
	@Test
	@Transactional
	public void whenGettingPlayerCards_GivenHasTwoCards_Then_ShouldReturnTwoCards() {
		createGameWithPlayer();
		addDeckToGame();
		
		List<PlayingCardView> cardsOfPlayer = playingCardService.getCardsOfPlayer(player.getUuid());
		assertThat(cardsOfPlayer, hasSize(0));
		
		PlayingCardView cardOne = playingCardService.dealCardToPlayer(player.getUuid());
		PlayingCardView cardTwo = playingCardService.dealCardToPlayer(player.getUuid());
		
		cardsOfPlayer = playingCardService.getCardsOfPlayer(player.getUuid());
		assertThat(cardsOfPlayer, hasSize(2));
		//TODO check if the returned cards are equal to dealed cards
	}
	
	@Test
	public void whenGetFacesSummary_Then_ShouldReturnCountOfCardsPerFace() {
		createGameWithPlayer();
		addDeckToGame();
		
		var summaries = playingCardService.getSuitsSummaryOfGame(game.getUuid());
		
		assertThat(summaries, hasSize(4));
		for (SuitSummaryView summary : summaries) {
			assertThat(summary, hasProperty("count", is(13L)));
		}
	}
	
	@Test
	public void whenGetSuitFacesSummary_Then_ShouldReturnCountOfCardsPerSuitAndFace() {
		createGameWithPlayer();
		addDeckToGame();
		addDeckToGame();
		
		var summaries = playingCardService.getSuitFacesSummaryOfGame(game.getUuid());
		
		assertThat(summaries, hasSize(52));
		assertThat(summaries.get(0), hasProperty("suit", is(CardSuit.HEARTS)));
		assertThat(summaries.get(0), hasProperty("face", is(CardFace.KING)));
		for (SuitFaceSummaryView summary : summaries) {
			assertThat(summary, hasProperty("count", is(2L)));
		}
	}
}
