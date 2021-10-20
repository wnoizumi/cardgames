package com.logmein.cardgames.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;

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
import com.logmein.cardgames.api.commands.CreateGameCommand;
import com.logmein.cardgames.api.views.DeckView;
import com.logmein.cardgames.api.views.GameView;
import com.logmein.cardgames.domain.repositories.DeckRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CardgamesApplication.class)
@AutoConfigureTestDatabase
@EnableAutoConfiguration
@Profile("test")
public class DeckServiceIntegrationTest {
	
	@Autowired
	private DeckService deckService;

	@Autowired
	private DeckRepository deckRepository;
	
	@After
	public void resetDb() {
		deckRepository.deleteAll();
	}
	
	@Test
	public void whenNewDeck_then_shouldCreateDeckWithCards() {
		CreateGameCommand command = new CreateGameCommand();
		command.name = null;
		
		DeckView deckView = deckService.newDeck();
		
		assertThat(deckView, hasProperty("uuid"));
	}
}
