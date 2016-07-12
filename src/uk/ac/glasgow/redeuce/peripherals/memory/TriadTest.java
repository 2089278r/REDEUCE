package uk.ac.glasgow.redeuce.peripherals.memory;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class TriadTest {
	
	Triad triad;
	
	
	@Before public void initialise() throws OutOfCardsException, IOException{
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
		CRDFileReader testReader = new CRDFileReader(file);
		FixedCardDeck newDeck = testReader.createNewDeck();
		DEUCECardReader reader = new DEUCECardReader();
		reader.loadDeck(newDeck);
		triad = new Triad(newDeck.getNextCard(), newDeck.getNextCard(), newDeck.getNextCard());
	}

	@Test
	public void positionTest() {
		assertEquals(triad.getPosition(), 0);
	}
	
	@Test
	public void delayLineTest() {
		int delayLine = triad.getDelayLine();
		assertEquals(delayLine, 7);
	}

}
