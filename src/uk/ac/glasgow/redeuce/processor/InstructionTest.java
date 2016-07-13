package uk.ac.glasgow.redeuce.processor;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.redeuce.memory.Word;

public class InstructionTest {
	
	BitSet bits = new BitSet(32);
	Instruction instruction;
	
	@Before public void initialise(){
		bits.set(1);
		bits.set(4);
		bits.set(5);
		bits.set(7);
		bits.set(8);
		bits.set(9);
		bits.set(11);
		bits.set(12);
		this.instruction = new Instruction(new Word(bits));
	}
	

	@Test
	public void toStringTest() {
		String output = instruction.toString();
		System.out.println(output);
		assertTrue(instruction.getNIS() == 1);
		assertTrue(instruction.getSource() == 27);
		assertTrue(instruction.getDest() == 13);
		assertTrue(instruction.getChar() == 0);
		assertTrue(instruction.getWait() == 0);
		assertTrue(instruction.getTiming() == 0);
		assertTrue(instruction.getGo() == 0);
		
	}

}
