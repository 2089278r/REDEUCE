package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class SingleRegisterTest {

	@Test
	public void wordsTest() {
		SingleRegister register = new SingleRegister();
		int word = register.numberOfWords;
		assertEquals(1, word);
	}
	
	@Test
	public void wordsTestTest() {
		SingleRegister register = new SingleRegister();
		int word = register.numberOfWords;
		assertNotSame(2, word);
	}
	
	@Test
	public void wordContentTest() {
		SingleRegister register = new SingleRegister();
		assertNotNull(register.contents);
	}
	
	@Test
	public void wordContentLength() {
		SingleRegister register = new SingleRegister();
		assertEquals(1, register.contents.length);
	}
	
	@Test
	public void wordContentValue() {
		SingleRegister register = new SingleRegister();
		assertEquals("00000000000000000000000000000000", register.contents[0]);
	}
	
	@Test
	public void wordWrite() {
		SingleRegister register = new SingleRegister();
		register.write("1");
		assertEquals("1", register.contents[0]);
	}
	
	@Test
	public void wordRead() {
		SingleRegister register = new SingleRegister();
		String registerValue = register.read();
		assertEquals(registerValue, register.contents[0]);
	}
	
	@Test
	public void wordWriteThenRead() {
		SingleRegister register = new SingleRegister();
		register.write("1");
		String registerValue = register.read();
		assertEquals("1", registerValue);
	}
	
	@Test
	public void writeFailOutOfPhase() {
		SingleRegister register = new SingleRegister();
		register.counter++;
		register.write("1");
		assertEquals("00000000000000000000000000000000", register.contents[0]);
	}
	
	@Test
	public void readFailOutOfPhase() {
		SingleRegister register = new SingleRegister();
		register.counter++;
		String valueHeld = register.read();
		assertNull(valueHeld);
	}

}
