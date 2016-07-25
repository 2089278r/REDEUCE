package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.redeuce.memory.QuadRegister;
import uk.ac.glasgow.redeuce.memory.Word;

public class QuadRegisterTest {
	
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
		register.increment();
		register.increment();
		register.increment();
		Word instruction = new Word(fullWord);
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



}
