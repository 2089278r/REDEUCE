package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class InstructionWordTest {

	@Test
	public void nisTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(7, example.getNIS());
	}
	@Test
	public void DestTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.getDest());
	}
	@Test
	public void SourceTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.getSource());
	}
	@Test
	public void CharTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(3, example.getChar());
	}
	@Test
	public void WaitTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.getWait());
	}
	@Test
	public void TimeTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.getTiming());
	}
	@Test
	public void goTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(1, example.getGo());
	}
	@Test(expected = Exception.class)
	public void invalidCard() throws Exception {
		int[] exampleArray = new int[] {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
		@SuppressWarnings("unused")
		InstructionWord example = new InstructionWord(exampleArray);
	}
	@Test(expected = Exception.class)
	public void shortCard() throws Exception {
		int[] examplearray = new int[] {0,1,1,1};
		@SuppressWarnings("unused")
		InstructionWord example = new InstructionWord(examplearray);
	}

}
