package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public abstract class MemoryUnit {
	int counter;
	int size;
	Word[] contents;
	//changes the value of the counter... Should it be here? Or perhaps processor should just have an array of all memory
	//which it loops around, incrementing each element accordingly
	public MemoryUnit(int size){
		this.size = size;
		this.counter = 0;
		this.contents = new Word[size];
		Arrays.fill(contents, new Word());
	}
	void increment(){
		counter++;
	}
	
	void write(Word word){
		contents[(counter % size)] = word;
	}
	
	Word read(){
		return contents[(counter % size)];
	}
	
}
