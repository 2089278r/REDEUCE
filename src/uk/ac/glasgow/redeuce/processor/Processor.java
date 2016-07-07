package uk.ac.glasgow.redeuce.processor;

import uk.ac.glasgow.redeuce.memory.Memory;

public class Processor {
	
	Instruction currentInstruction;
	//Word fromReader;
	boolean go;
	Memory deuceMemory; //For testing purposes I suppose? I guess we'll have a large object later where the processor can just read memory?
	
	public Processor(){
		//this.fromReader = fromReader;
		this.go = true;
		this.currentInstruction = new Instruction(deuceMemory.getWord(1));
	}
	public void TickClock(){
		deuceMemory.increment();
	}
	
	public Instruction getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void getNextInstruction(){
		 int nextDelayLine = currentInstruction.getNIS();
		 this.currentInstruction = new Instruction(deuceMemory.getWord(nextDelayLine));
	}
	
	public boolean getGo(){
		return this.go;
	}
	
	public void executeInstruction(){
		int source = this.currentInstruction.getSource();
		int dest = this.currentInstruction.getDest();
		int characteristic = this.currentInstruction.getChar();
		int wait = this.currentInstruction.getWait();
		int timing = this.currentInstruction.getTiming();
		int go = this.currentInstruction.getGo();
		
		// Huge nasty switch statement, or at least something which defines the types?
	}

}
