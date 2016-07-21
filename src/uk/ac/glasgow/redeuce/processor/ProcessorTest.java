package uk.ac.glasgow.redeuce.processor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.redeuce.DeuceConstants;
import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.memory.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.memory.DEUCECardPuncher;
import uk.ac.glasgow.redeuce.peripherals.memory.DEUCECardReader;
import uk.ac.glasgow.redeuce.peripherals.memory.FixedCardDeck;
import uk.ac.glasgow.redeuce.peripherals.memory.OutOfCardsException;

public class ProcessorTest {
	
	String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
	CRDFileReader testReader = new CRDFileReader(file);
	DEUCECardReader reader = new DEUCECardReader();
	DEUCECardPuncher puncher = new DEUCECardPuncher();
	Memory deuceMemory = new Memory();
	Processor proc;
	
	@Before public void initialise() throws OutOfCardsException, IOException{
		FixedCardDeck newDeck = testReader.createNewDeck();
		reader.loadDeck(newDeck);
		proc = new Processor(reader, deuceMemory, puncher);
	}
	
	//Tests that an instruction is written correctly
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
	
	//Tests that a simple addition happens, regardless of timing and so on
	@Test 
	public void arithmeticTest1() throws InterruptedException{
		
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		Instruction testArith = new Instruction(0, 27, 25, 0, 0, 0, 0);
		proc.currentInstruction = testArith;
		proc.executeArithmetic(testArith);
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 1);
	}
	
	//Tests that correct arithmetic happens with a normal delay line
	@Test
	public void arithmeticTest2(){
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		proc.deuceMemory.setWord(4, new Word(70));
		Instruction testArith = new Instruction(0, 4, 25, 0, 0, 0, 0);   //Wait and normal ticks and so on are irrelevant here, as that only occurs in executeinstruction
		proc.executeArithmetic(testArith);
		// HAD TO BE CHANGED, AS TICKS NOW HAPPEN IN EXECUTEINSTRUCTION
		assertEquals(70, proc.deuceMemory.getWord(13).getAsInt());
	}
	
	//Tests that the correct arithmetic happens in an actual instruction setting
	@Test
	public void ExecuteArithmeticTest2() throws InterruptedException, IOException{
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
		proc.deuceMemory.setWord(4, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 4, 25, 0, 30, 0, 0);
		proc.setCurrentInstruction(testArith);
		proc.executeInstruction();
		assertEquals(70, proc.deuceMemory.getWord(13).getAsInt());
	}
	
	//Tests that correct arithmetic happens in DoubleStore case as well, with the other word still being 0
	@Test
	public void arithmeticTest3(){
		assertTrue(proc.deuceMemory.getWord(21).getAsInt() == 0);
		proc.deuceMemory.setWord(5, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 5, 22, 0, 30, 0, 0);
		proc.executeArithmetic(testArith);
		// HAD TO BE CHANGED, AS TICKS NOW HAPPEN IN EXECUTEINSTRUCTION
		assertEquals(70, proc.deuceMemory.getWord(21).getAsInt());
		proc.tickClock();
		assertEquals(0, proc.deuceMemory.getWord(21).getAsInt());
	}
	
	//Tests that the above test words as an executeInstruction as well
	@Test
	public void executeArithmeticTest3() throws InterruptedException, IOException{
		assertTrue(proc.deuceMemory.getWord(21).getAsInt() == 0);
		proc.deuceMemory.setWord(5, new Word(70));
		//wait 30 so we can go back to the original place where the word was stored
		Instruction testArith = new Instruction(0, 5, 22, 0, 30, 0, 0);
		proc.setCurrentInstruction(testArith);
		proc.executeInstruction();
		// HAD TO BE CHANGED, AS TICKS NOW HAPPEN IN EXECUTEINSTRUCTION
		assertEquals(70, proc.deuceMemory.getWord(21).getAsInt());
	}
	
	//Tests that a single transfer happens in the most basic case, time and wait are 0
	@Test
	public void singleTransferTest() throws InterruptedException, IOException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		Instruction testTransfer = new Instruction(0, 4, 5, 0, 0, 2, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		assertEquals(4, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 2){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Tests that a single transfer happens on the correct mc depending on the wait and timing numbers
	@Test
	public void singleTransferTestWait() throws InterruptedException, IOException {
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
		assertEquals(4, proc.deuceMemory.getMicroCycle());               //Should be at 4, starting at 0, 2(wait) + 2(setup) and then transfer
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());       //Should now be in 5'4'
	}
	
	
	//Tests that a single transfer happens, and it ends at the correct time
	@Test
	public void singleTransferTestWaitTime() throws InterruptedException, IOException {
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
		assertEquals(6, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Tests that a single transfer happens correctly when wait is bigger than timing
	@Test
	public void singleTransferTestBigWait() throws InterruptedException, IOException {
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
		assertEquals(4, proc.deuceMemory.getMicroCycle());
		proc.tickClock();
		proc.tickClock();
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
		proc.tickClock();
		assertEquals(0, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Tests that a double transfer happens correctly in the most basic case
	// NOTE: Actually not as basic because a double transfer will be different when T=W
	// SECOND NOTE: Actually, it still works fine
	@Test
	public void doubleTransferTest() throws InterruptedException, IOException {
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
//		while(proc.deuceMemory.getMicroCycle() != 2){
//			proc.tickClock();
//		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
		proc.tickClock();
		assertEquals(450, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Test to determine that a double transfer happens correctly in the case of wait and time being equal
	@Test
	public void doubleTransferTestWait() throws InterruptedException, IOException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400)); //(4'4' == 400)
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(450)); //(4'5' == 450)
		Instruction testTransfer = new Instruction(0, 4, 5, 2, 2, 2, 0); //(instruction is move from what's in 4'4 and 4'5 into 5'4 and 5'5)
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();                                      
		assertEquals(4, proc.deuceMemory.getMicroCycle());               //Should be at 4, starting at 0, 2(setup) + 2(wait) and then transfer
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());       //Should now be in 5'4'
		proc.tickClock();
		assertEquals(450, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Tests that the correct word gets written into mc4 and 5, and that the timing take the instruction ends on mc6
	@Test
	public void doubleTransferTestWaitTime() throws InterruptedException, IOException {
		assertTrue(proc.deuceMemory.getWord(4).getAsInt() == 0);
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(400));
		proc.tickClock();
		proc.deuceMemory.setWord(4, new Word(430));
		Instruction testTransfer = new Instruction(0, 4, 5, 2, 2, 4, 0);
		proc.setCurrentInstruction(testTransfer);
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.executeInstruction();
		assertEquals(6, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 4){
			proc.tickClock();
		}
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
		proc.tickClock();
		assertEquals(430, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Tests that a double transfer works in the case that the wait number is bigger than the timing
	@Test
	public void doubleTransferTestBigWait() throws InterruptedException, IOException {
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
		assertEquals(4, proc.deuceMemory.getMicroCycle());
		proc.tickClock();
		proc.tickClock();
		assertEquals(400, proc.deuceMemory.getWord(5).getAsInt());
		proc.tickClock();
		assertEquals(0, proc.deuceMemory.getWord(5).getAsInt());
	}
	
	//Tests that arithmetic works correctly in the case of a single transfer
	@Test
	public void singleArithmeticTestWait() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		Instruction testArith = new Instruction(0, 2, DeuceConstants.DEST_SINGLE_ADD, 0, 2, 0, 0);
		proc.setCurrentInstruction(testArith);
		while(proc.deuceMemory.getMicroCycle() != 30){
			proc.tickClock();
		}
		proc.executeInstruction(); // should be at 2'2, so the wait should bring it here from 30
		for (int i=0; i<32; i++)
			assertEquals(20, proc.deuceMemory.getWord(13).getAsInt()); //Should always be the same value at any cycle
		
	}
	
	//Tests that addition works in the case of a double transfer
	@Test
	public void doubleArithmeticTestWait() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(10));
		Instruction testArith = new Instruction(0, 2, DeuceConstants.DEST_SINGLE_ADD, 2, 2, 0, 0);
		proc.setCurrentInstruction(testArith);
		while(proc.deuceMemory.getMicroCycle() != 30){
			proc.tickClock();
		}
		proc.executeInstruction(); // should be at 2'2, so the wait should bring it here from 30
		for (int i=0; i<32; i++)
			assertEquals(30, proc.deuceMemory.getWord(13).getAsInt()); //Should always be the same value at any cycle
		
	}
	
	//Tests if adding during a long transfer works correctly, taking numbers from different words in a delay line and adding them
	@Test
	public void longArithmeticTestWait() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(10));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(5));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(2));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(1));
		Instruction testArith = new Instruction(0, 2, DeuceConstants.DEST_SINGLE_ADD, 1, 2, 6, 0);
		proc.setCurrentInstruction(testArith);
		while(proc.deuceMemory.getMicroCycle() != 30){
			proc.tickClock();
		}
		proc.executeInstruction(); // should be at 2'2, so the wait should bring it here from 30
		for (int i=0; i<32; i++)
			assertEquals(38, proc.deuceMemory.getWord(13).getAsInt()); //Should always be the same value at any cycle
	}
	
	//Tests that long transfers word for subtraction as well (given that it works for addition, above
	@Test
	public void longSubTestWait() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(20));
		proc.deuceMemory.setWord(13, new Word(100));
		Instruction testArith = new Instruction(0, 2, DeuceConstants.DEST_SINGLE_SUB, 1, 2, 6, 0);
		proc.setCurrentInstruction(testArith);
		while(proc.deuceMemory.getMicroCycle() != 30){
			proc.tickClock();
		}
		proc.executeInstruction(); // should be at 2'2, so the wait should bring it here from 30
		for (int i=0; i<32; i++)
			assertEquals(0, proc.deuceMemory.getWord(13).getAsInt()); //Should always be the same value at any cycle
	}
	
	
	//Tests that the memory is one step further in the case that the source word in a discrim_zero instruction is not a 0
	@Test
	public void discrimTest() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(1));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		Instruction discrimInstr = new Instruction(2, 2, DeuceConstants.DEST_DISCRIM_ZERO, 0, 0, 0, 0);
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(3, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(0));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(2, proc.deuceMemory.getMicroCycle());
	}
	
	//Test that when the word is not 0, there is a delay of 1 additional timing, but not in the case that it is equal to 0
	@Test
	public void discrimTestWithTiming() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(1));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		Instruction discrimInstr = new Instruction(2, 2, DeuceConstants.DEST_DISCRIM_ZERO, 0, 0, 2, 0);
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(5, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(0));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(4, proc.deuceMemory.getMicroCycle());
	}
	
	//Test to see if two subsequent words that need to be checked for discrim instructions can be done.
	//Works, but should be noted that I have no idea if this is how it's intended to work...
	@Test
	public void discrimTestDouble() throws InterruptedException, IOException {
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(1));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(1));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		Instruction discrimInstr = new Instruction(2, 2, DeuceConstants.DEST_DISCRIM_ZERO, 2, 0, 2, 0);
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(6, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(0));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(0));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(4, proc.deuceMemory.getMicroCycle());
	}
	
	//Test that the other discrim instruction also works in some capacity
	@Test
	public void discrimNegativeTest() throws InterruptedException, IOException{
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(Integer.MAX_VALUE));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		Instruction discrimInstr = new Instruction(2, 2, DeuceConstants.DEST_DISCRIM_SIGN, 2, 0, 2, 0);
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(5, proc.deuceMemory.getMicroCycle());
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(100000));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.setCurrentInstruction(discrimInstr);
		proc.executeInstruction();
		assertEquals(4, proc.deuceMemory.getMicroCycle());
	}
	
	//Test to see that IO instructions are working in some manner, leading to System.outs
	@Test
	public void ioTestPrintOut() throws InterruptedException, IOException{
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(500));
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		Instruction ioInstr = new Instruction(2, 2, DeuceConstants.DEST_PUNCHOUT, 0, 0, 0, 0);
		proc.setCurrentInstruction(ioInstr);
		proc.executeInstruction();
		assertEquals(2, proc.deuceMemory.getMicroCycle());
	}
	
	//Test to see that a long characteristic can be used to print out multiple lines in a DL correctly
	@Test
	public void ioTestLongPrintOut() throws InterruptedException, IOException{
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(500));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(505));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(510));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(515));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(520));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(525));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(530));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(535));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(540));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(545));
		proc.tickClock();
		proc.deuceMemory.setWord(2, new Word(550));
		
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		Instruction ioInstr = new Instruction(2, 2, DeuceConstants.DEST_PUNCHOUT, 1, 0, 10, 0);
		proc.setCurrentInstruction(ioInstr);
		proc.executeInstruction();
		assertEquals(12, proc.deuceMemory.getMicroCycle());
	}
	
	//The other IO instructions are not really going to be used, or at least have no implementations yet, so I'm unsure as to how testing them should go...
	
	
	//Tests that the shifted left function returns the right value
	@Test
	public void divByTwoTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(14, new Word(500));
		Instruction getTSdiv2 = new Instruction(0, DeuceConstants.SOURCE_SINGLESTORE_HALVED, 5, 0, 0, 0, 0);
		proc.setCurrentInstruction(getTSdiv2);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(5).getAsInt(), 250);
	}
	
	//Tests that what is returned is double the number from before (everything shifted to the right in backwards binary)
	@Test
	public void mulByTwoTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(14, new Word(500));
		Instruction getTSmult2 = new Instruction(0, DeuceConstants.SOURCE_STORE_DOUBLED, 5, 0, 0, 0, 0);
		proc.setCurrentInstruction(getTSmult2);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(5).getAsInt(), 1000);
	}
	
	//Tests that 10111001 AND 10011111 returns 10011001
	@Test
	public void logicAndTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(14, new Word(185));
		proc.deuceMemory.setWord(15, new Word(159));
		Instruction logicAnd = new Instruction(0, DeuceConstants.SOURCE_AND, 5, 0, 0, 0, 0);
		proc.setCurrentInstruction(logicAnd);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(5).getAsInt(), 153);
	}
	
	//Tests that 10111001 XOR 10011111 returns 00100110
	@Test
	public void logicXORTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(14, new Word(185));
		proc.deuceMemory.setWord(15, new Word(159));
		Instruction logicxor = new Instruction(0, DeuceConstants.SOURCE_XOR, 5, 0, 0, 0, 0);
		proc.setCurrentInstruction(logicxor);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(5).getAsInt(), 38);
	}
	
	//Tests that source 27 contains 1
	@Test
	public void constantOneTest() throws InterruptedException, IOException{
		Instruction single = new Instruction(0, DeuceConstants.SOURCE_CONSTANT_ONE, 5, 0, 0, 0, 0);
		proc.setCurrentInstruction(single);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(5).getAsInt(), 1);
	}
	
	//Tests that source 30 contains 0 by rewriting a value already held
	public void zeroTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(14, new Word(185));
		assertEquals(proc.deuceMemory.getWord(14).getAsInt(), 185);
		Instruction logicxor = new Instruction(0, DeuceConstants.SOURCE_CONSTANT_ZERO, 14, 0, 0, 0, 0);
		proc.setCurrentInstruction(logicxor);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(14).getAsInt(), 0);
	}
	
	//Tests that source 31 contains -1, or the max int value, so adding should reduce the number by one
	public void maximumTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(14, new Word(185));
		assertEquals(proc.deuceMemory.getWord(14).getAsInt(), 185);
		Instruction maximum = new Instruction(0, DeuceConstants.SOURCE_CONSTANT_NEGATIVE, 14, 0, 0, 0, 0);
		proc.setCurrentInstruction(maximum);
		proc.executeInstruction();
		assertEquals(proc.deuceMemory.getWord(14).getAsInt(), 184);
	}
	
	//Tests that the correct next instruction is retrieved after executing another
	@Test
	public void nextInstrTest() throws InterruptedException, IOException{
		Word word = new Instruction(2, DeuceConstants.SOURCE_CONSTANT_ONE, 13, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word);
		proc.tickClock();
		proc.tickClock();
		Word nextWord = new Instruction(3, DeuceConstants.SOURCE_CONSTANT_ONE, DeuceConstants.DEST_SINGLE_ADD, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(2, nextWord);
		proc.tickClock();
		proc.tickClock();
		Word thirdWord = new Instruction (1, 13, DeuceConstants.DEST_PUNCHOUT, 0, 0, 26, 0).getAsWord();
		proc.deuceMemory.setWord(3, thirdWord);
		while (proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.setCurrentInstruction(new Instruction(word));
		for (int i=0; i<10; i++){
			proc.step();
		}
	}
	
	@Test
	public void simpleLoopTest() throws InterruptedException, IOException{
		proc.deuceMemory.setWord(13, new Word(5));
		Word word1 = new Instruction (3, 27, 26, 0, 0, 0, 0).getAsWord();
		Word word2 = new Instruction (2, 13, DeuceConstants.DEST_PUNCHOUT, 0, 0, 0, 0).getAsWord();
		Word word3 = new Instruction (1, 13, DeuceConstants.DEST_DISCRIM_ZERO, 0, 0, 25, 0).getAsWord();
		Word word4 = new Instruction (1, DeuceConstants.SOURCE_CONSTANT_NEGATIVE, DeuceConstants.DEST_PUNCHOUT, 0, 0, 0, 1).getAsWord();
		proc.deuceMemory.setWord(1, word2);
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(2, word1);
		proc.tickClock();
		proc.tickClock();
		proc.deuceMemory.setWord(3, word3);
		while (proc.deuceMemory.getMicroCycle() != 31){
			proc.tickClock();
		}
		proc.deuceMemory.setWord(1, word4);
		proc.tickClock();
		proc.setCurrentInstruction(new Instruction(word2));
		for (int i=0; i<16; i++){
			proc.step();
		}
		assertTrue(proc.deuceMemory.getWord(13).getAsInt() == 0);
	}
	
	@Test
	public void perfectSquaresBigTest() throws InterruptedException, IOException{
		Word word1 = new Instruction(1, 27, 13, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word1);      //mc 0
		proc.tickClock();
		proc.tickClock();
		Word word2 = new Instruction(1, 27, 25, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word2);      //mc 2
		proc.tickClock();
		proc.tickClock();
		Word word3 = new Instruction(1, 13, 14, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word3);      //mc 4
		proc.tickClock();
		proc.tickClock();
		Word word4 = new Instruction(1, 27, 15, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word4);      //mc 6
		proc.tickClock();
		proc.tickClock();
		Word word5 = new Instruction(1, 27, 16, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word5);      //mc 8
		proc.tickClock();
		proc.tickClock();
		
		//LOOP
		Word word6 = new Instruction(1, 16, 29, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word6);      //mc 10
		proc.tickClock();
		
		//Should end up here, mc11, after as many squares as possible given a 32-bit word are printed
		Word finalWord = new Instruction (1, DeuceConstants.SOURCE_CONSTANT_NEGATIVE, DeuceConstants.DEST_PUNCHOUT, 0, 0, 0, 1).getAsWord();
		proc.deuceMemory.setWord(1, finalWord);
		proc.tickClock();
		////////////////////////
		
		Word word7 = new Instruction(1, 14, 13, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word7);      //mc 12
		proc.tickClock();
		proc.tickClock();
		Word word8 = new Instruction(1, 15, 25, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word8);      //mc 14
		proc.tickClock();
		proc.tickClock();
		Word word9 = new Instruction(1, 13, 15, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word9);      //mc 16
		proc.tickClock();
		proc.tickClock();
		Word word10 = new Instruction(1, 16, 25, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word10);      //mc 18
		proc.tickClock();
		proc.tickClock();
		Word word11 = new Instruction(1, 13, 16, 0, 0, 0, 0).getAsWord();
		proc.deuceMemory.setWord(1, word11);      //mc 20
		proc.tickClock();
		proc.tickClock();
		Word word12 = new Instruction(1, 13, DeuceConstants.DEST_DISCRIM_SIGN, 0, 0, 18, 0).getAsWord();
		proc.deuceMemory.setWord(1, word12);      //mc 22
		while(proc.deuceMemory.getMicroCycle() != 0){
			proc.tickClock();
		}
		proc.setCurrentInstruction(new Instruction(word1));
		while(true){
			proc.step();
			if(proc.currentInstruction.getGo() == 1){
				proc.step();
				assertTrue(true);
				System.out.println("yaaaaaay!");
				break;
			}
		}
	}
}
