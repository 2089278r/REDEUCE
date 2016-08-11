package uk.ac.glasgow.redeuce.memory;

/*
 * An abstract store which can hold a certain amount of words
 * Implemented as Delay Lines and Registers in the Memory class.
 */

public abstract class MemoryUnit {
	protected int counter;
	protected int size;
	protected Word[] contents;
	//changes the value of the counter... Should it be here? Or perhaps processor should just have an array of all memory
	//which it loops around, incrementing each element accordingly
	public MemoryUnit(int size){
		this.size = size;
		this.counter = 0;
		this.contents = new Word[size];
		for (int i=0; i<size; i++){
			contents[i] = new Word();
		}
	}
	void increment(){
		assert(counter < Integer.MAX_VALUE);
		counter++;
	}
	
	void write(Word word){
		contents[(counter % size)] = word;
	}
	
	Word read(){
		return contents[(counter % size)];
	}
	
	public String makeString(){
		String output = "";
		for (int i=0; i<size; i++){
			output += contents[i].toString() + " ";
		}
		return output;
	}
	
}
