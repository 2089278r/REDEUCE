package uk.ac.glasgow.redeuce.peripherals.memory;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class CRDFileReaderTest {

	@Test
	public void creationTest() throws IOException {
		CRDFileReader testReader = new CRDFileReader("/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd");
		testReader.createNewDeck();
	}

}
