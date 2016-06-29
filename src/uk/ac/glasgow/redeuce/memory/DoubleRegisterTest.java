package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class DoubleRegisterTest {

	@Test
	public void wordsTest() {
		DoubleRegister register = new DoubleRegister();
		int word = register.numberOfWords;
		assertEquals(2, word);
	}
	
	@Test
	public void wordsTestTest() {
		DoubleRegister register = new DoubleRegister();
		int word = register.numberOfWords;
		assertNotSame(3, word);
	}
	
	@Test
	public void wordContentTest() {
		DoubleRegister register = new DoubleRegister();
		assertNotNull(register.contents);
	}
	
	@Test
	public void wordContentLength() {
		DoubleRegister register = new DoubleRegister();
		assertEquals(2, register.contents.length);
	}
	
	@Test
	public void wordContentValue() {
		DoubleRegister register = new DoubleRegister();
		for (int i=0; i < register.numberOfWords ; i++){ 
			assertEquals("00000000000000000000000000000000", register.contents[i]);
		}
	}
	
	@Test
	public void wordWrite() {
		DoubleRegister register = new DoubleRegister();
		register.counter++;
		register.write("1");
		assertEquals("1", register.contents[1]);
	}
	
	@Test
	public void wordRead() {
		DoubleRegister register = new DoubleRegister();
		register.counter++;
		String registerValue = register.read();
		assertEquals(registerValue, register.contents[1]);
	}
	
	@Test
	public void wordWriteThenRead() {
		DoubleRegister register = new DoubleRegister();
		register.counter++;
		register.write("1");
		String registerValue = register.read();
		assertEquals("1", registerValue);
	}
	
	@Test
	public void wordWriteThenReadPrevious() {
		DoubleRegister register = new DoubleRegister();
		register.counter++;
		register.write("1");
		register.counter--;
		String registerValue = register.read();
		assertNotEquals("1", registerValue);
	}
	
	@Test
	public void writeFailOutOfPhase() {
		DoubleRegister register = new DoubleRegister();
		register.counter = 3;
		register.write("1");
		for (int i=0; i < register.numberOfWords ; i++){ 
			assertEquals("00000000000000000000000000000000", register.contents[i]);
		}
	}
	
	@Test
	public void readFailOutOfPhase() {
		DoubleRegister register = new DoubleRegister();
		register.counter = 3;
		String valueHeld = register.read();
		assertNull(valueHeld);
	}
	
	public void writeSucceedsModulo() {
		QuadRegister register = new QuadRegister();
		register.counter = 33;
		register.write("1");
		assertEquals("1", register.contents[1]);
	}

}
