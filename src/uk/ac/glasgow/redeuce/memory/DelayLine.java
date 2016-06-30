package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class DelayLine extends MemoryUnit{
	
	public DelayLine(){
		int numberOfWords = 32;
		contents = new Word[numberOfWords];
		Arrays.fill(contents, "00000000000000000000000000000000");
	}
	
	void write(Word word){
		int line = (counter % 32);
		contents[line] = word;
	}
	
	public Word read(){
		int currentMC = (counter % 32);
		return contents[currentMC];
	}
}
