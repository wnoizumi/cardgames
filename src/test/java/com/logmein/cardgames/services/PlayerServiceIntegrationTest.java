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
import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.commands.DeckGameAssociationCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.PlayerView;
import com.logmein.cardgames.api.views.PlayingCardView;
import com.logmein.cardgames.domain.entities.Game;
import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.repositories.GameRepository;
import com.logmein.cardgames.domain.repositories.PlayerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CardgamesApplication.class)
@AutoConfigureTestDatabase
@EnableAutoConfiguration
@Profile("test")
public class PlayerServiceIntegrationTest {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private DeckService deckService;
	
	@Autowired
	private PlayingCardService playingCardService;
	
	@After
	public void resetDb() {
		playerRepository.deleteAll();
		gameRepository.deleteAll();
	}

	@Test
	public void whenAddNewPlayerToGame_Then_ShouldSaveToDb() {
		List<Player> all = playerRepository.findAll();
		assertThat(all, hasSize(0));
		
		Game game = gameRepository.save(new Game("Game 1"));
		AddPlayerCommand command = new AddPlayerCommand();
		command.gameUuid = game.getUuid();
		command.name = "Player 1";
		PlayerView playerView = playerService.addPlayerToGame(command);
		
		assertThat(playerView, hasProperty("name", is("Player 1")));
		assertThat(playerView, hasProperty("uuid"));
		
		all = playerRepository.findAll();
		assertThat(all, hasSize(1));
	}
	
	@Test
	public void whenDeleteExistingPlayer_Then_ShouldRemoveFromDb() {
		Game existingGame = gameRepository.save(new Game("Game 1"));
		Player player = playerRepository.save(new Player("Player 1", existingGame));
		
		List<Player> all = playerRepository.findAll();
		
		assertThat(all, hasSize(1));

		playerService.deletePlayer(player.getUuid());
		
		all = playerRepository.findAll();
		
		assertThat(all, hasSize(0));
	}
	
	@Test
	@Transactional
	public void whenGettingPlayersOfGame_GivenTwoPlayers_Then_ShouldReturnPlayersSortedByHand() {
		Game game = gameRepository.saveAndFlush(new Game("game 1"));
		Player playerOne = playerRepository.save(new Player("Player 1", game));
		Player playerTwo = playerRepository.save(new Player("Player 2", game));
		
		DeckView deckView = deckService.newDeck();
		DeckGameAssociationCommand command = new DeckGameAssociationCommand();
		command.deckUuid = deckView.getUuid();
		
		gameService.addDeckToGame(game.getUuid(), command);
		
		PlayingCardView cardViewOne = playingCardService.dealCardToPlayer(playerOne.getUuid());
		PlayingCardView cardViewTwo = playingCardService.dealCardToPlayer(playerOne.getUuid());
		
		List<PlayerView> playersOfGame = playerService.getPlayersOfGame(game.getUuid());
		
		int handValuePlayerOne = cardViewOne.getFace().getFaceValue() + cardViewTwo.getFace().getFaceValue();
		
		assertThat(playersOfGame, hasSize(2));
		assertThat(playersOfGame.get(0), hasProperty("handValue", is(handValuePlayerOne)));
		assertThat(playersOfGame.get(0), hasProperty("uuid", is(playerOne.getUuid())));
		assertThat(playersOfGame.get(0), hasProperty("name", is(playerOne.getName())));
		assertThat(playersOfGame.get(1), hasProperty("handValue", is(0)));
		assertThat(playersOfGame.get(1), hasProperty("uuid", is(playerTwo.getUuid())));
		assertThat(playersOfGame.get(1), hasProperty("name", is(playerTwo.getName())));
	}
}
