package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class InstructionWordTest {
	
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
	public void nisTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(7, example.getNIS());
	}
	@Test
	public void DestTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(31, example.getDest());
	}
	@Test
	public void SourceTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(31, example.getSource());
	}
	@Test
	public void CharTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(3, example.getChar());
	}
	@Test
	public void WaitTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(31, example.getWait());
	}
	@Test
	public void TimeTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(31, example.getTiming());
	}
	@Test
	public void goTest() throws Exception {
		InstructionWord example = new InstructionWord(fullWord);
		assertEquals(1, example.getGo());
	}
	


}
