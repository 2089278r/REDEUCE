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
	
	Triad triad;
	CRDFileReader testReader;
	FixedCardDeck newDeck;
	DEUCECardReader reader;
	Processor proc;

	@Before public void initialise() throws OutOfCardsException, IOException{
		String file = "/users/level3/2089278r/INTERN/REDEUCE/src/uk/ac/glasgow/redeuce/peripherals/memory/prog07DH.crd";
		this.testReader = new CRDFileReader(file);
		this.newDeck = testReader.createNewDeck();
		this.reader = new DEUCECardReader();
		this.proc = new Processor(reader);
		//this.deuceMemory = new Memory();
		reader.loadDeck(newDeck);
	}
	
	//Making sure the right destination places were printed out; seems like memory is working
	@Test
	public void readLinesTest() throws OutOfCardsException{
		proc.initialInput();
		while (!(proc.getCounter() == 0)){
			this.proc.tickClock();
		}
		for (int i=0; i<32; i++){
			System.out.println(proc.getCounter() + ": " + proc.getWord(7).getElements(4, 9));
			this.proc.tickClock();
		}
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
		System.out.println(instr.toString());
	}
	
	@Test
	public void transferTest() throws InterruptedException{
		BitSet bits = new BitSet(32);
		//0100 1100 0001
		bits.set(1);
		bits.set(4);
		bits.set(5);
		bits.set(11);
		proc.deuceMemory.setWord(1, new Word(bits));
		proc.deuceMemory.setWord(3, new Word(bits));
		proc.tickClock();
		proc.tickClock();
		BitSet otherBits = new BitSet(32);
		otherBits.set(1);
		otherBits.set(4);
		otherBits.set(5);
		otherBits.set(11);
		otherBits.set(31);
		proc.deuceMemory.setWord(1, new Word(otherBits));
		while(proc.deuceMemory.getMicroCycle() != 0){
			System.out.println("Microcycle: " + proc.deuceMemory.getMicroCycle() + " has word " + proc.deuceMemory.getWord(1).getBits());
			proc.tickClock();
		}
		Instruction instr = new Instruction(proc.deuceMemory.getWord(1));
		proc.currentInstruction = instr;
		proc.executeInstruction();
		assertEquals(1, proc.currentInstruction.getGo());
	}
	
	

}
