package uk.ac.glasgow.redeuce.memory;

/*
 * Represents the whole memory of the DEUCE. 
 * 12 32-bit delay lines
 * 4 Single-word Registers
 * 2 Quadruple-word Registers
 * 3 Double-word Registers
 * 
 */

public class Memory {
	private MemoryUnit[] linesAndStores;
	
	public Memory(){
		//DL0 was actually DL8, so there is a +1 to make implementation easier later on...
		this.linesAndStores = new MemoryUnit[22]; 
		for (int i=1; i<=12; i++){
			linesAndStores[i] = new DelayLine();
		}
		for (int i=13; i<=16; i++){
			linesAndStores[i] = new SingleRegister();
		}
		for (int i=17; i<=18; i++){
			linesAndStores[i] = new QuadRegister();
		}
		for (int i=19; i<=21; i++){
			linesAndStores[i] = new DoubleRegister();
		}
	}
	
	public void increment(){
		for (int i=1; i<linesAndStores.length; i++){
			linesAndStores[i].increment();
		}
	}
	
	public int getMicroCycle(){
		return ((linesAndStores[1].counter) % 32);
	}
	
	public Word getWord(int delayLine){
		return linesAndStores[delayLine].read();
	}
	
	public void setWord(int toDelayLine, Word word){
		linesAndStores[toDelayLine].write(word);
	}
	
	//USED ONLY FOR CONSOLETEST OUTPUT, HENCE ONLY SHOWING 4 REGISTERS
	public String toString(){
		StringBuilder output = new StringBuilder();
		for (int i=13; i<=16; i++){
			output.append(linesAndStores[i].makeString());
			output.append("\n");
		}
		return output.toString();
	}
	
	public String outputRegisters(){
		StringBuilder output = new StringBuilder();
		for (int i=13; i<=21; i++){
			output.append(linesAndStores[i].makeString());
			output.append(" ");
		}
		return output.toString();
	}
	
	//Method to return contents in a delay line for the Displays
	public String outputDelayLine(int delayLine, int offset){
		StringBuilder delayStore = new StringBuilder();
		for (int i=0; i<32; i++){
			delayStore.append(linesAndStores[delayLine].contents[(i + offset)%32].toString());
			delayStore.append(" ");
		}
		return delayStore.toString();
	}
	
	//Method to set all words in the memory to 0s. Used in the CLEAR STORE function
	public void clear(){
		for (int i=1; i<linesAndStores.length; i++){
			for(int j = 0; j<32; j++){
				setWord(i, new Word(0));
				increment();
			}
		}
	}
}
