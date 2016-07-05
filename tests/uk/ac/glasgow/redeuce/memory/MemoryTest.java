package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void test() {
		Memory memory = new Memory();
		for (int i=1; i<memory.linesAndStores.length; i++){
			assertTrue(memory.linesAndStores[i] instanceof MemoryUnit);
		}
	}
}
