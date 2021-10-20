package com.logmein.cardgames.domain.builders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.logmein.cardgames.domain.entities.Deck;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeckBuilderUnitTest {
	
	@Autowired
	private DeckBuilder deckBuilder;
	
	@Test
	public void whenBuildDeck_shouldReturn_deckWith52Cards() {
		Deck deck = deckBuilder.buildDeck();
		
		Assert.assertEquals(deck.getCards().size(), 52);
	}
}
