package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class DoubleRegisterTest {

	@Test
	public void sizeTest() {
		DoubleRegister register = new DoubleRegister();
		assertEquals(2, register.contents.length);
	}
	
	@Test
	public void wordsTestTest() {
		DoubleRegister register = new DoubleRegister();
		assertNotSame(3, register.contents.length);
	}
	
	@Test
	public void wordContentTest() {
		DoubleRegister register = new DoubleRegister();
		assertNotNull(register.contents);
	}
	
	@Test
	public void wordWrite() {
		DoubleRegister register = new DoubleRegister();
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		register.increment();
		Word instruction = new Word(examplearray);
		register.write(instruction);
		assertEquals(instruction, register.contents[1]);
	}
	
	@Test
	public void wordRead() {
		DoubleRegister register = new DoubleRegister();
		register.increment();
		Word registerValue = register.read();
		assertEquals(registerValue, register.contents[1]);
	}
	
	@Test
	public void wordWriteThenRead() {
		DoubleRegister register = new DoubleRegister();
		register.increment();
		register.write(new Word());
		Word registerValue = register.read();
		assertEquals(register.contents[1].binaryDigits, registerValue.binaryDigits);
	}

}

