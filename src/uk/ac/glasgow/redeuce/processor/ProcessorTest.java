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
		assertTrue(instr.getNIS() == 1);
		assertTrue(instr.getChar() == 0);
		assertTrue(instr.getDest() == 13);
		assertTrue(instr.getWait() == 0);
		assertTrue(instr.getGo() == 0);
		assertTrue(instr.getTiming() == 0);
	}
	
	@Test 
	public void arithmeticTest1() throws InterruptedException{
		
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		Instruction testArith = new Instruction(0, 27, 25, 0, 0, 0, 0);
		proc.currentInstruction = testArith;
		proc.executeArithmetic(testArith);
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 1);
	}
	
	@Test
	public void executeArithmeticTest1() throws InterruptedException{
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		Instruction testArith = new Instruction(0, 27, 25, 0, 0, 0, 0);
		proc.currentInstruction = testArith;
		proc.executeInstruction();
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
	public void ExecuteArithmeticTest2() throws InterruptedException{
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		proc.deuceMemory.setWord(4, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 4, 25, 0, 30, 0, 0);
		proc.setCurrentInstruction(testArith);
		proc.executeInstruction();
		//proc.tickClock();
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
	
	@Test
	public void executeArithmeticTest3() throws InterruptedException{
		assertTrue(proc.deuceMemory.getWord(21).getAsInt() == 0);
		proc.deuceMemory.setWord(5, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 5, 22, 0, 30, 0, 0);
		proc.setCurrentInstruction(testArith);
		proc.executeInstruction();
		// HAD TO BE CHANGED, AS TICKS NOW HAPPEN IN EXECUTEINSTRUCTION
		assertEquals(70, proc.deuceMemory.getWord(21).getAsInt());
	}
	
	@Test
	public void singleTransferTest() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		Instruction testTransfer = new Instruction(0, 4, 5, 0, 0, 0, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		while(proc.deuceMemory.getMicroCycle() != 2){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	@Test
	public void singleTransferTestWait() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400)); //(4'4' == 400)
		Instruction testTransfer = new Instruction(0, 4, 5, 0, 2, 2, 0); //(instruction is move from what's in 4 into 5)
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();                                      
		assertEquals(6, proc.deuceMemory.getMicroCycle());               //Should be at 4, starting at 0, 2(setup) + 2(wait) and then transfer
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());       //Should now be in 5'4'
	}
	
	@Test
	public void singleTransferTestWaitTime() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		Instruction testTransfer = new Instruction(0, 4, 5, 0, 2, 4, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		assertEquals(8, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	@Test
	public void singleTransferTestBigWait() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		Instruction testTransfer = new Instruction(0, 4, 5, 0, 4, 2, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		assertEquals(6, proc.deuceMemory.getMicroCycle());
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	

	@Test
	public void doubleTransferTest() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(450));
		Instruction testTransfer = new Instruction(0, 4, 5, 2, 0, 0, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		while(proc.deuceMemory.getMicroCycle() != 2){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
		proc.tickClock();
		assertEquals(450, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	@Test
	public void doubleTransferTestWait() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400)); //(4'4' == 400)
		Instruction testTransfer = new Instruction(0, 4, 5, 2, 2, 2, 0); //(instruction is move from what's in 4 into 5)
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();                                      
		assertEquals(6, proc.deuceMemory.getMicroCycle());               //Should be at 4, starting at 0, 2(setup) + 2(wait) and then transfer
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());       //Should now be in 5'4'
	}
	
	@Test
	public void doubleTransferTestWaitTime() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		Instruction testTransfer = new Instruction(0, 4, 5, 2, 2, 4, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		assertEquals(8, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	@Test
	public void doubleTransferTestBigWait() throws InterruptedException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		Instruction testTransfer = new Instruction(0, 4, 5, 2, 4, 2, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		assertEquals(6, proc.deuceMemory.getMicroCycle());
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	
}
