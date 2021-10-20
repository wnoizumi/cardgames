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

import com.logmein.cardgames.CardgamesApplication;
import com.logmein.cardgames.api.commands.AddPlayerCommand;
import com.logmein.cardgames.api.views.PlayerView;
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
	private PlayerService playerService;
	
	@After
	public void resetDb() {
		playerRepository.deleteAll();
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
}
