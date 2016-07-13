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
import uk.ac.glasgow.redeuce.processor.Instruction.instructionType;


// Make just about everything that isn't called from outside Private

public class Processor {
	
	Instruction currentInstruction;
	DEUCECardReader reader;
	int currentDelayLine; //Just for testing afaik
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
		 this.currentInstruction = new Instruction(deuceMemory.getWord(nextDelayLine)); //To keep track of where we are (maybe just for testing...)
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
		int numberOfExecutions;
		
		// Huge nasty switch statement, or at least something which defines the types?
		
		switch(characteristic){
			case 1:
				numberOfExecutions = (timing - wait + 1);
				break;
			case 2:
				numberOfExecutions = 2;
		}
	
		switch(currentInstruction.getType()){
			case ARITHMETIC:
				executeArithmetic();
				break;
			case DISCRIM:
				executeDiscrim();
				break;
			case IO:
				executeIO();
				break;
			case OTHER:
				
				break;
			case TRANSFER:
				executeTransfer();
				break;
			default:
				System.out.println("Something must've gone wrong...");
				break;
		}
		
		if (go == 1){
			return;
		}
		getNextInstruction();
//		executeInstruction();
	}
	
	//Won't do when timing gets involved
	//Preferably tickClock will only be called in the execution function
	public void transfer(int source, int destination, int numberOfExecutions){
		Word from = this.deuceMemory.getWord(source);
		this.deuceMemory.setWord(destination, from);
	}
	
	public int readyDelayLine() throws OutOfCardsException{
		reader.takeInCards();
		Triad currentTriad = reader.getTriad();
		int delayLine = currentTriad.getDelayLine();
		return delayLine;
	}
	
	//Change names 
	//Read as long as there is another Triad (another while)
	public void initialInput() throws OutOfCardsException {
		while(!reader.isEmpty()){
			int delayLine = readyDelayLine();
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
