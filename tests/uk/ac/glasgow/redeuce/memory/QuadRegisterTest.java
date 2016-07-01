package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class QuadRegisterTest {

	@Test
	public void sizeTest() {
		QuadRegister register = new QuadRegister();
		assertEquals(4, register.contents.length);
	}
	
	@Test
	public void wordsTestTest() {
		QuadRegister register = new QuadRegister();
		assertNotSame(6, register.contents.length);
	}
	
	@Test
	public void wordContentTest() {
		QuadRegister register = new QuadRegister();
		assertNotNull(register.contents);
	}
	
	@Test
	public void wordWrite() {
		QuadRegister register = new QuadRegister();
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		register.increment();
		register.increment();
		register.increment();
		Word instruction = new Word(examplearray);
		register.write(instruction);
		assertEquals(instruction, register.contents[3]);
	}
	
	@Test
	public void wordRead() {
		QuadRegister register = new QuadRegister();
		register.increment();
		register.increment();
		register.increment();
		Word registerValue = register.read();
		assertEquals(registerValue, register.contents[3]);
	}
	
	@Test
	public void wordWriteThenRead() {
		QuadRegister register = new QuadRegister();
		register.increment();
		register.increment();
		register.increment();
		register.write(new Word());
		Word registerValue = register.read();
		assertEquals(register.contents[3].binaryDigits, registerValue.binaryDigits);
	}



}
