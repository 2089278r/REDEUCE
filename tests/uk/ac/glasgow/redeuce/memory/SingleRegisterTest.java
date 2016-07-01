package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class SingleRegisterTest {

	@Test
	public void sizeTest() {
		SingleRegister register = new SingleRegister();
		assertEquals(1, register.contents.length);
	}
	
	@Test
	public void wordsTestTest() {
		SingleRegister register = new SingleRegister();
		assertNotSame(2, register.contents.length);
	}
	
	@Test
	public void wordContentTest() {
		SingleRegister register = new SingleRegister();
		assertNotNull(register.contents);
	}
	
	@Test
	public void wordWrite() {
		SingleRegister register = new SingleRegister();
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		Word instruction = new Word(examplearray);
		register.write(instruction);
		assertEquals(instruction, register.contents[0]);
	}
	
	@Test
	public void wordRead() {
		SingleRegister register = new SingleRegister();
		Word registerValue = register.read();
		assertEquals(registerValue, register.contents[0]);
	}
	
	@Test
	public void wordWriteThenRead() {
		SingleRegister register = new SingleRegister();
		register.write(new Word());
		Word registerValue = register.read();
		assertEquals(register.contents[0].binaryDigits, registerValue.binaryDigits);
	}

}
