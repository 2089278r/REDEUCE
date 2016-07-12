package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import java.util.BitSet;

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
		BitSet examplearray = new BitSet();
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
	

}

