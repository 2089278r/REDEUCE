package uk.ac.glasgow.redeuce.peripherals;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class DEUCECardReaderTest {

	@Test
	public void getDelayLineTest() throws IOException, OutOfCardsException {
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		DEUCECardReader reader = new DEUCECardReader();
		reader.loadDeck(newDeck);
		reader.takeInCards();
		int delayLine = reader.getTriad().getDelayLine();
		assertEquals(delayLine, 7);
	}
	
	@Test
	public void takeInCardsTest() throws OutOfCardsException, IOException{
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		DEUCECardReader reader = new DEUCECardReader();
		reader.loadDeck(newDeck);
		reader.takeInCards();
		assertTrue(reader.triad.getCurrentCard().getNextLine().getBits().cardinality() == 4);	
	}
	
	@Test
	public void readFirstCardTest() throws OutOfCardsException, IOException{
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		DEUCECardReader reader = new DEUCECardReader();
		reader.loadDeck(newDeck);
		reader.takeInCards();
		assertTrue(true);
	}
}
