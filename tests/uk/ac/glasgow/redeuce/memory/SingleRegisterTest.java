package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class SingleRegisterTest {

	BitSet fullWord;

	@Before public void initialise(){
		fullWord = new BitSet(32);
		for (int i=1; i<=20; i++){
			fullWord.set(i);
		}
		for (int i=25; i<=29; i++){
			fullWord.set(i);
		}
		fullWord.set(31);
		
	}
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
		Word instruction = new Word(fullWord);
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
		assertEquals(register.contents[0].getElements(0, 31), registerValue.getElements(0, 31));
	}

}
