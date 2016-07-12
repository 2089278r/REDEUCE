package uk.ac.glasgow.redeuce.processor;

import java.util.BitSet;
import java.util.concurrent.TimeUnit;

import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.memory.Card;
import uk.ac.glasgow.redeuce.peripherals.memory.CardLine;
import uk.ac.glasgow.redeuce.peripherals.memory.DEUCECardReader;
import uk.ac.glasgow.redeuce.peripherals.memory.OutOfCardsException;
import uk.ac.glasgow.redeuce.peripherals.memory.Triad;

public class Processor {
	
	Instruction currentInstruction;
	DEUCECardReader reader;
	int currentDelayLine;
	boolean go;
	Memory deuceMemory; //For testing purposes I suppose? I guess we'll have a large object later where the processor can just read memory?
	
	public Processor(DEUCECardReader reader){
		this.reader = reader;
		this.go = true;
		this.deuceMemory = new Memory();
	}
	public void tickClock(){
		deuceMemory.increment();
	}
	
	public int getCounter(){
		return deuceMemory.getMicroCycle();
	}
	
	public Instruction getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public Word getWord(int delayLine){
		return deuceMemory.getWord(delayLine);
	}
	
	public void getNextInstruction(){
		 int nextDelayLine = currentInstruction.getNIS();
		 this.currentInstruction = new Instruction(deuceMemory.getWord(nextDelayLine), nextDelayLine); //To keep track of where we are (maybe just for testing...)
	}
	
	public boolean getGo(){
		return this.go;
	}
	
	public void executeInstruction() throws InterruptedException{
		int source = this.currentInstruction.getSource();
		int dest = this.currentInstruction.getDest();
		int characteristic = this.currentInstruction.getChar();
		int wait = this.currentInstruction.getWait();
		int timing = this.currentInstruction.getTiming();
		int go = this.currentInstruction.getGo();
		
		// Huge nasty switch statement, or at least something which defines the types?
		if ((source <= 21) && (dest <= 21)){
			transfer(source, dest);
		}
		
		if (go == 1){
			return;
		}
		getNextInstruction();
		executeInstruction();
	}
	
	public void transfer(int source, int destination){
		Word from = this.deuceMemory.getWord(source);
		this.deuceMemory.setWord(destination, from);
		tickClock();
		tickClock();
	}
	
	public int initialInput() throws OutOfCardsException{
		reader.takeInCards();
		Triad currentTriad = reader.getTriad();
		int delayLine = currentTriad.getDelayLine();
		return delayLine;
	}
	
	public void readLines() throws OutOfCardsException {
		int delayLine = initialInput();
		Card currentCard = reader.getTriad().getCurrentCard();
		CardLine currentLine;
		while (currentCard != null){
			currentLine = currentCard.getNextLine();
			while (currentLine != null){
				deuceMemory.setWord(delayLine, new Word(currentLine.getBits()));
				tickClock();
				currentLine = currentCard.getNextLine();
			}
			currentCard = reader.getTriad().getNext();
		}
		
	}
	
	

}
