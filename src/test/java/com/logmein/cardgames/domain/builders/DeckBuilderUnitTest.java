package com.logmein.cardgames.domain.builders;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import com.logmein.cardgames.domain.entities.Deck;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("test")
public class DeckBuilderUnitTest {
	
	@Autowired
	private DeckBuilder deckBuilder;
	
	@Test
	public void whenBuildDeck_ShouldReturn_DeckWith52Cards() {
		Deck deck = deckBuilder.buildDeck();
		
		assertThat(deck.getCards(), hasSize(52));
	}
}
