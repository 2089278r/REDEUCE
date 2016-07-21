package uk.ac.glasgow.redeuce.peripherals.memory;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class CRDFileReaderTest {

	@Test
	public void ExampleDeckTest() throws IOException {
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		assertTrue(newDeck.cards.size() == 13);
	}
	
	@Test
	public void ExampleCardTest() throws IOException {
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		for (int i=0; i<newDeck.cards.size(); i++){
			assertTrue(newDeck.cards.get(i).getSize() == 12);
		}
	}
	
	@Test
	public void ExampleCardRowTest() throws Exception {
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		Card firstCard = newDeck.getNextCard();
		CardLine testRow = firstCard.getNextLine();
		testRow = firstCard.getNextLine();
		assertEquals(testRow.getBits().get(3), true);
		testRow = firstCard.getNextLine();
		testRow = firstCard.getNextLine();
		testRow = firstCard.getNextLine();
		assertTrue(testRow.getBits().get(4) == true);
	}
	
	@Test(expected=OutOfCardsException.class)
	public void EventuallyRunsOutTest() throws OutOfCardsException, IOException{
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		@SuppressWarnings("unused")
		Card currentCard = newDeck.getNextCard();
		while(true){
			currentCard = newDeck.getNextCard();
		}
		
	}

}
