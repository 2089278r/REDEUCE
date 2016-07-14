package uk.ac.glasgow.redeuce.processor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.memory.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.memory.DEUCECardReader;
import uk.ac.glasgow.redeuce.peripherals.memory.FixedCardDeck;
import uk.ac.glasgow.redeuce.peripherals.memory.OutOfCardsException;
import uk.ac.glasgow.redeuce.peripherals.memory.Triad;

public class ProcessorTest {
	
	String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
	CRDFileReader testReader = new CRDFileReader(file);
	DEUCECardReader reader = new DEUCECardReader();
	Memory deuceMemory = new Memory();
	Processor proc = new Processor(reader, deuceMemory);
	
	@Before public void initialise() throws OutOfCardsException, IOException{
		FixedCardDeck newDeck = testReader.createNewDeck();
		reader.loadDeck(newDeck);
	}
	
	//Making sure the right destination places were printed out; seems like memory is working
//	@Test
//	public void readLinesTest() throws OutOfCardsException{
//		proc.initialInput();
//		while (!(proc.getCounter() == 0)){
//			this.proc.tickClock();
//		}
//		for (int i=0; i<32; i++){
//			System.out.println(proc.getCounter() + ": " + proc.getWord(7).getElements(4, 9));
//			this.proc.tickClock();
//		}
//	}
	
	@Test
	public void readsInstructionsTest(){
		Memory memory = new Memory();
		BitSet bits = new BitSet(32);
		bits.set(1);
		bits.set(4);
		bits.set(5);
		bits.set(7);
		bits.set(8);
		bits.set(9);
		bits.set(11);
		bits.set(12);
		memory.setWord(1, new Word(bits));
		Instruction instr = new Instruction(memory.getWord(1));
		//System.out.println(instr.toString());
	}
	
	@Test 
	public void arithmeticTest1() throws InterruptedException{
		
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		Instruction testArith = new Instruction(0, 27, 25, 0, 0, 0, 0);
		proc.currentInstruction = testArith;
		proc.executeInstruction();
		System.out.println(proc.deuceMemory.getMicroCycle());
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 1);
	}
	
	@Test
	public void arithmeticTest2(){
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		proc.deuceMemory.setWord(4, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 4, 25, 0, 32, 0, 0);
		proc.executeArithmetic(testArith);
		// HAD TO BE CHANGED, AS TICKS NOW HAPPEN IN EXECUTEINSTRUCTION
		assertEquals(70, proc.deuceMemory.getWord(13).getAsInt());
	}
	
	@Test
	public void arithmeticTest3(){
		assertTrue(proc.deuceMemory.getWord(21).getAsInt() == 0);
		proc.deuceMemory.setWord(5, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 5, 22, 0, 32, 0, 0);
		proc.executeArithmetic(testArith);
		// HAD TO BE CHANGED, AS TICKS NOW HAPPEN IN EXECUTEINSTRUCTION
		assertEquals(70, proc.deuceMemory.getWord(21).getAsInt());
	}
	
	
	

}
