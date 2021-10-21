package com.logmein.cardgames.api;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logmein.cardgames.CardgamesApplication;
import com.logmein.cardgames.domain.repositories.DeckRepository;
import com.logmein.cardgames.services.DeckService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CardgamesApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@EnableAutoConfiguration
public class DecksApiIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private DeckService deckService;
	
	@Autowired
	private DeckRepository repository;
	
	 @Autowired 
	 private ObjectMapper mapper;
	
	@After
	public void resetDb() {
		repository.deleteAll();
	}
	
	@Test
	public void whenAll_thenShouldReturnAllDecksFromDb() throws Exception {
		deckService.newDeck();
		deckService.newDeck();
		
		mvc.perform(get("/api/decks").contentType(MediaTypes.HAL_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
		.andExpect(jsonPath("$._embedded.decks", hasSize(greaterThanOrEqualTo(2))))
		.andReturn();
		//TODO check content and links
	}

}
