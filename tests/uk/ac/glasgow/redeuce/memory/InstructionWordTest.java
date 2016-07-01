package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class InstructionWordTest {

	@Test
	public void nisTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(7, example.nis);
	}
	@Test
	public void DestTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.dest);
	}
	@Test
	public void SourceTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.source);
	}
	@Test
	public void CharTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(3, example.characteristic);
	}
	@Test
	public void WaitTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.wait);
	}
	@Test
	public void TimeTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(31, example.timing);
	}
	@Test
	public void goTest() throws Exception {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		assertEquals(1, example.go);
	}
	@Test(expected = Exception.class)
	public void invalidCard() throws Exception {
		int[] exampleArray = new int[] {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
		InstructionWord example = new InstructionWord(exampleArray);
	}
	@Test(expected = Exception.class)
	public void shortCard() throws Exception {
		int[] examplearray = new int[] {0,1,1,1};
		InstructionWord example = new InstructionWord(examplearray);
	}

}
