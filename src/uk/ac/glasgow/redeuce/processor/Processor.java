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
	
	public Word getSource(Instruction instr){
		Word operand;
		BitSet newWordBits;
		BitSet oldWord;
		if (instr.getSource() >21){
			switch (instr.getSource()){
				case 22: 
					newWordBits = new BitSet();
					oldWord = deuceMemory.getWord(21).getBits();
					//Not really just a "move left" operation, sadly... I hope this'll do?
					for (int i=0; i<oldWord.length(); i++){
						if(oldWord.get(i+1) == true){
							newWordBits.set(i);
						}
					}
					operand = new Word(newWordBits);
					return operand;
					break;
				case 23:
					newWordBits = new BitSet();
					oldWord = deuceMemory.getWord(14).getBits();
					//Not really just a "move left" operation, sadly... I hope this'll do?
					for (int i=0; i<oldWord.length(); i++){
						if(oldWord.get(i+1) == true){
							newWordBits.set(i);
						}
					}
					operand = new Word(newWordBits);
					return operand;
				case 24:
					newWordBits = new BitSet();
					oldWord = deuceMemory.getWord(21).getBits();
					//Not really just a "move right" operation, sadly... I hope this'll do?
					for (int i=1; i<=oldWord.length(); i++){
						if(oldWord.get(i-1) == true){
							newWordBits.set(i);
						}
					}
					operand = new Word(newWordBits);
					return operand;
				case 25:
					newWordBits = deuceMemory.getWord(14).getBits();
					oldWord = deuceMemory.getWord(15).getBits();
					newWordBits.and(oldWord);
					operand = new Word(newWordBits);
					return operand;
				case 26:
					newWordBits = deuceMemory.getWord(14).getBits();
					oldWord = deuceMemory.getWord(15).getBits();
					newWordBits.xor(oldWord);
					operand = new Word(newWordBits);
					return operand;
				case 27:
					BitSet newBits = new BitSet(32);
					newBits.set(0);
					operand = new Word(newBits);
					return operand;
				case 28:
					//Honestly, this one makes no sense to me
					break;
				case 29:
					//Again, just no idea what the manual means
					break;
				case 30:
					operand = new Word(new BitSet(32));
					return operand;
				case 31:
					BitSet negativeOne = new BitSet(32);
					for (int i=0; i<32; i++){
						negativeOne.set(i);
					}
					operand = new Word(negativeOne);
					return operand;
				default:
					System.out.println("uhhhh...");
					break;
				}
		}
		else {
			operand = deuceMemory.getWord(instr.getSource());
			return operand;
		}
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
	
		switch(currentInstruction.getType()){
			case ARITHMETIC:
				executeArithmetic(currentInstruction);
				break;
			case DISCRIM:
				executeDiscrim(currentInstruction);
				break;
			case IO:
				executeIO(currentInstruction);
				break;
			case OTHER:
				break;
			case TRANSFER:
				executeTransfer(currentInstruction);
				break;
			default:
				System.out.println("Something must've gone wrong...");
		}
	}
	
	//All examples only involve a Temporary store and a Delay Line, or some function other than just writing...
	//So I honestly have little confidence in what "long transfers" of read/writes actually did in practice...
	public void executeTransfer(Instruction instruction){
			//Maybe this works for our looping concerns?
			int necessaryTicks = instruction.getTiming() + 2;
			tickClock();
			tickClock();
			for (int i=0; i<instruction.getWait(); i++){
				tickClock();
				necessaryTicks--;
			}
			for (int i=0; i<(getNumberOfExecutions(instruction)) ; i++){
				Word from = this.deuceMemory.getWord(instruction.getSource());
				this.deuceMemory.setWord(instruction.getDest(), from);
				necessaryTicks--;
			}
			while (necessaryTicks > 0){
				tickClock();
			}
			
		}
	
	
	public void executeArithmetic(Instruction instruction){	
		//Get source, check which kind of arithmetic, execute accordingly
	}
	
	public void executeDiscrim(Instruction instruction){
		//Get source, check which discrimination instruction it is, execute accordingly
	}
	
	public void executeIO(Instruction instruction){
		//Get source, check which IO instruction it is, execute accordingly
	}
	
	
	//A function for the execution instructions to run to see how many times they need to repeat.
	//Not yet tested, but it seems like it'd solve our Characteristic problem perhaps?
	public int getNumberOfExecutions(Instruction instr){
		if (instr.getChar() == 2){
			return 2;
		}
		else if (instr.getChar() == 1){
			return (instr.getTiming() - instr.getWait() + 1);
		}
		else {
			return 1;
		}
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
