package uk.ac.glasgow.redeuce.userinterface.console;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.glasgow.redeuce.peripherals.OutOfCardsException;

public class ConsoleTest {
	
	Console myConsole;
	
	@Before public void initialise(){
		this.myConsole = new Console();
	}
	
	@Test
	public void test() throws InterruptedException, IOException, OutOfCardsException {
		myConsole.menu();
		assertTrue(true);
	}

}
