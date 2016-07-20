package uk.ac.glasgow.redeuce.processor;

import java.util.BitSet;

import uk.ac.glasgow.redeuce.DeuceConstants;
import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.memory.Card;
import uk.ac.glasgow.redeuce.peripherals.memory.CardLine;
import uk.ac.glasgow.redeuce.peripherals.memory.DEUCECardReader;
import uk.ac.glasgow.redeuce.peripherals.memory.OutOfCardsException;
import uk.ac.glasgow.redeuce.peripherals.memory.Triad;


// Make just about everything that isn't called from outside Private

public class Processor {
	
	Instruction currentInstruction;
	DEUCECardReader reader;
	int currentDelayLine; //Just for testing afaik
	boolean go;
	Memory deuceMemory; //For testing purposes I suppose? I guess we'll have a large object later where the processor can just read memory?
	boolean tcb;
	
	public Processor(DEUCECardReader reader, Memory deuceMemory){
		this.reader = reader;
		this.go = true;
		this.deuceMemory = deuceMemory;
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
		 this.currentInstruction = new Instruction(deuceMemory.getWord(nextDelayLine));
	}
	
	public void setCurrentInstruction(Instruction instruction){
		this.currentInstruction = instruction;
	}
	
	public boolean getGo(){
		return this.go;
	}
	
	public Word analyseSource(Instruction instr){
		Word operand;
		BitSet newWordBits;
		BitSet oldWord;
		switch (instr.getSource()) {
		case DeuceConstants.SOURCE_DOUBLESTORE_HALVED:
			newWordBits = new BitSet();
			oldWord = deuceMemory.getWord(21).getBits();
			// Not really just a "move left" operation, sadly... I hope this'll
			// do?
			for (int i = 0; i < oldWord.length(); i++) {
				if (oldWord.get(i + 1) == true) {
					newWordBits.set(i);
				}
			}
			operand = new Word(newWordBits);
			return operand;
		case DeuceConstants.SOURCE_SINGLESTORE_HALVED:
			newWordBits = new BitSet();
			oldWord = deuceMemory.getWord(14).getBits();
			// Not really just a "move left" operation, sadly... I hope this'll
			// do?
			for (int i = 0; i < oldWord.length(); i++) {
				if (oldWord.get(i + 1) == true) {
					newWordBits.set(i);
				}
			}
			operand = new Word(newWordBits);
			return operand;
		case DeuceConstants.SOURCE_STORE_DOUBLED:
			newWordBits = new BitSet();
			oldWord = deuceMemory.getWord(21).getBits();
			// Not really just a "move right" operation, sadly... I hope this'll
			// do?
			for (int i = 1; i <= oldWord.length(); i++) {
				if (oldWord.get(i - 1) == true) {
					newWordBits.set(i);
				}
			}
			operand = new Word(newWordBits);
			return operand;
		case DeuceConstants.SOURCE_AND:
			newWordBits = deuceMemory.getWord(14).getBits();
			oldWord = deuceMemory.getWord(15).getBits();
			newWordBits.and(oldWord);
			operand = new Word(newWordBits);
			return operand;
		case DeuceConstants.SOURCE_XOR:
			newWordBits = deuceMemory.getWord(14).getBits();
			oldWord = deuceMemory.getWord(15).getBits();
			newWordBits.xor(oldWord);
			operand = new Word(newWordBits);
			return operand;
		case DeuceConstants.SOURCE_CONSTANT_ONE:
			BitSet newBits = new BitSet(32);
			newBits.set(0); //makes least significant digit true, or 1
			operand = new Word(newBits);
			return operand;
		case DeuceConstants.SOURCE_CONSTANT_WAIT:
			// Honestly, this one makes no sense to me
			break;
		case DeuceConstants.SOURCE_CONSTANT_HIGHEST:
			// Again, just no idea what the manual means
			break;
		case DeuceConstants.SOURCE_CONSTANT_ZERO:
			operand = new Word(new BitSet(32));
			return operand;
		case DeuceConstants.SOURCE_CONSTANT_NEGATIVE:
			BitSet negativeOne = new BitSet(32);
			for (int i = 0; i < 32; i++) {
				negativeOne.set(i);
			}
			operand = new Word(negativeOne);
			return operand;
		default:
			operand = deuceMemory.getWord(instr.getSource());
			return operand;
		}
		operand = new Word(0);
		return operand;
	}
	
	public void executeInstruction() throws InterruptedException{
		// Huge nasty switch statement, or at least something which defines the types?
	
		for (int i=0; i<currentInstruction.getWait(); i++){
			tickClock();
		}
		
		//setup
		tickClock();
		//System.out.println("ticked" + deuceMemory.getMicroCycle());
		tickClock();
		//System.out.println("ticked" + deuceMemory.getMicroCycle());
	
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
			int necessaryTicks;
			if (((instruction.getWait() > instruction.getTiming()) || ((instruction.getWait() == instruction.getTiming()) && (instruction.getChar() == 2)))){
				necessaryTicks = instruction.getTiming() + 32;
			}
			else {
				necessaryTicks = instruction.getTiming();
			}
			necessaryTicks -= instruction.getWait();
			for (int i=0; i<(getNumberOfExecutions(instruction)) ; i++){
				Word from = analyseSource(instruction);
				this.deuceMemory.setWord(instruction.getDest(), from);
				if(necessaryTicks != 0){
					tickClock();
					necessaryTicks--;
				}
			}
			while (necessaryTicks > 0){
				tickClock();
				necessaryTicks--;
			}

		}
	
	
	public void executeArithmetic(Instruction instruction){	
		//Get source, check which kind of arithmetic, execute accordingly
		int necessaryTicks;
		if (((instruction.getWait() > instruction.getTiming()) || ((instruction.getWait() == instruction.getTiming()) && (instruction.getChar() == 2)))){
			necessaryTicks = instruction.getTiming() + 32;
		}
		else {
			necessaryTicks = instruction.getTiming();
		}
		necessaryTicks -= instruction.getWait();
		for (int i=0; i<(getNumberOfExecutions(instruction)) ; i++){
			Word from = analyseSource(instruction);
			int dest = instruction.getDest();
			int before;
			int after;
			Word newWord;
			switch(dest){
			//DOUBLE ADD and DOUBLE SUB will need to be implemented with the TCB in mind... will rework when discussed!
			case DeuceConstants.DEST_DOUBLE_SUB:
				before = deuceMemory.getWord(21).getAsInt();
				after = (before - from.getAsInt());
				newWord = new Word(after);	
				deuceMemory.setWord(21, newWord);
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			case DeuceConstants.DEST_DOUBLE_ADD:
				before = deuceMemory.getWord(21).getAsInt();
				after = (before + from.getAsInt());
				newWord = new Word(after);
				deuceMemory.setWord(21, newWord);
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			case DeuceConstants.DEST_SINGLE_ADD:
				before = deuceMemory.getWord(13).getAsInt();
				after = (before + from.getAsInt());
				newWord = new Word(after);
				deuceMemory.setWord(13, newWord);
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			case DeuceConstants.DEST_SINGLE_SUB:
				before = deuceMemory.getWord(13).getAsInt();
				after = (before - from.getAsInt());
				newWord = new Word(after);
				deuceMemory.setWord(13, newWord);
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			}
		}
		
		while (necessaryTicks > 0){
			tickClock();
			necessaryTicks--;
		}
	}
	
	public void executeDiscrim(Instruction instruction){
		//Get source, check which discrimination instruction it is, execute accordingly
		int necessaryTicks;
		if (((instruction.getWait() > instruction.getTiming()) || ((instruction.getWait() == instruction.getTiming()) && (instruction.getChar() == 2)))){
			necessaryTicks = instruction.getTiming() + 32;
		}
		else {
			necessaryTicks = instruction.getTiming();
		}
		necessaryTicks -= instruction.getWait();
		for (int i=0; i<(getNumberOfExecutions(instruction)) ; i++){
			int dest = instruction.getDest();
			Word sourceWord = analyseSource(instruction);
			int toBeChecked = sourceWord.getAsInt();
			switch(dest){
			case(DeuceConstants.DEST_DISCRIM_SIGN):
				if (toBeChecked == Integer.MAX_VALUE){
					necessaryTicks++;
				}
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			case(DeuceConstants.DEST_DISCRIM_ZERO):
				if (toBeChecked != 0){
					necessaryTicks++;
				}
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			}
		}
		while (necessaryTicks > 0){
			tickClock();
			necessaryTicks--;
		}
	}
	
	public void executeIO(Instruction instruction){
		//Get source, check which IO instruction it is, execute accordingly
		int necessaryTicks;
		if (((instruction.getWait() > instruction.getTiming()) || ((instruction.getWait() == instruction.getTiming()) && (instruction.getChar() == 2)))){
			necessaryTicks = instruction.getTiming() + 32;
		}
		else {
			necessaryTicks = instruction.getTiming();
		}
		necessaryTicks -= instruction.getWait();
		for (int i=0; i<(getNumberOfExecutions(instruction)) ; i++){
			Word from = analyseSource(instruction);
			int dest = instruction.getDest();
			switch(dest){
			case(DeuceConstants.DEST_INPUT_OUTPUT):
				if(instruction.getSource() == 9){
					//if CardReader.isReading{
					//   turnoff();
					//if CardPuncher.isReady(){
					//   turnoff();
					//alternatively, turn off all peripherals if we so choose to represent them such
					if(necessaryTicks > 0){
						tickClock();
						necessaryTicks--;
					}
					break;
				}
				else if (instruction.getSource() == 10){
					//reader.loadDeck(deck); maybe ? 
					if(necessaryTicks > 0){
						tickClock();
						necessaryTicks--;
					}
					break;
				}
				else if (instruction.getSource() == 12){
					//puncher.start(); or something like that
					if(necessaryTicks > 0){
						tickClock();
						necessaryTicks--;
					}
					break;
				}
				else {
					if(necessaryTicks > 0){
						tickClock();
						necessaryTicks--;
					}
					break;
				}
			case(DeuceConstants.DEST_PUNCHOUT):
				System.out.println(from.getAsInt()); //assuming we want decimals punched out?
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				// In the end System I guess we'd be calling some sort of CardPuncher function, giving the source word as an argument?
				break;
			case(DeuceConstants.DEST_READ_WRITE):
				//Mystery Magnetic Store things...
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			case(DeuceConstants.DEST_HEADS_MOVE):
				//More mysterious magnetic store writing/reading heads stuff....
				if(necessaryTicks > 0){
					tickClock();
					necessaryTicks--;
				}
				break;
			}
		}
		while (necessaryTicks > 0){
			tickClock();
			necessaryTicks--;
		}
		
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
	
	public void initialInput() throws OutOfCardsException {
		reader.takeInCards();
		Triad currentTriad = reader.getTriad();
		int delayLine = currentTriad.getDelayLine();
		while(currentTriad != null){
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
			reader.takeInCards();
			currentTriad = reader.getTriad();
		}
	}
	
	public void settcb(){
		this.tcb = true;
	}
	
	public void offtcb(){
		this.tcb = false;
	}
	
	public void step() throws InterruptedException{
		executeInstruction();
		
		//Added this here for testing; should NOT be in final!
		
		if (this.currentInstruction.getGo() == 1){
			return;
		}
		getNextInstruction();
	}
}
