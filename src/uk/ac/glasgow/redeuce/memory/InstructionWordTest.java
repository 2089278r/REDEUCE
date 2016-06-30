package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class InstructionWordTest {

	@Test
	public void test() {
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		InstructionWord example = new InstructionWord(examplearray);
		System.out.println(example.nis);
		System.out.println(example.source);
		System.out.println(example.dest);
		System.out.println(example.characteristic);
		System.out.println(example.wait);
		System.out.println(example.timing);
		System.out.println(example.go);
		assertEquals(true, true);
	}

}
