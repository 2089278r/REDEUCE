package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class QuadRegisterTest {

	@Test
	public void wordsTest() {
		QuadRegister register = new QuadRegister();
		int word = register.numberOfWords;
		assertEquals(4, word);
	}
	
	@Test
	public void wordsTestTest() {
		QuadRegister register = new QuadRegister();
		int word = register.numberOfWords;
		assertNotSame(3, word);
	}
	
	@Test
	public void wordContentTest() {
		QuadRegister register = new QuadRegister();
		assertNotNull(register.contents);
	}
	
	@Test
	public void wordContentLength() {
		QuadRegister register = new QuadRegister();
		assertEquals(4, register.contents.length);
	}
	
	@Test
	public void wordContentValue() {
		QuadRegister register = new QuadRegister();
		for (int i=0; i < register.numberOfWords ; i++){ 
			assertEquals("00000000000000000000000000000000", register.contents[i]);
		}
	}
	
	@Test
	public void wordWrite() {
		QuadRegister register = new QuadRegister();
		register.counter++;
		register.write("1");
		assertEquals("1", register.contents[1]);
	}
	
	@Test
	public void wordRead() {
		QuadRegister register = new QuadRegister();
		register.counter++;
		String registerValue = register.read();
		assertEquals(registerValue, register.contents[1]);
	}
	
	@Test
	public void wordWriteThenRead() {
		QuadRegister register = new QuadRegister();
		register.counter++;
		register.write("1");
		String registerValue = register.read();
		assertEquals("1", registerValue);
	}
	
	@Test
	public void wordWriteThenReadPrevious() {
		QuadRegister register = new QuadRegister();
		register.counter = 3;
		register.write("1");
		register.counter--;
		String registerValue = register.read();
		assertNotEquals("1", registerValue);
	}
	
	@Test
	public void writeFailOutOfPhase() {
		QuadRegister register = new QuadRegister();
		register.counter = 5;
		register.write("1");
		for (int i=0; i < register.numberOfWords ; i++){ 
			assertEquals("00000000000000000000000000000000", register.contents[i]);
		}
	}
	
	@Test
	public void writeSucceedsModulo() {
		QuadRegister register = new QuadRegister();
		register.counter = 35;
		register.write("1");
		assertEquals("1", register.contents[3]);
	}
	
	@Test
	public void readFailOutOfPhase() {
		QuadRegister register = new QuadRegister();
		register.counter = 5;
		String valueHeld = register.read();
		assertNull(valueHeld);
	}


}
