package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public abstract class Register extends MemoryUnit {
	int numberOfWords;
	
	public Register(){
		contents = new String[numberOfWords];
		Arrays.fill(contents, "000000000000000000000000000000000");
	}
	
	public void write(String word){
		int currentMC = (counter % 32);
		if (currentMC < numberOfWords){
			// Check it's data rather than an instruction maybe?
			contents[currentMC] = word;
		}
	}
	
	public String read(){
		int currentMC = (counter % 32);
		if (currentMC < numberOfWords){
			return contents[currentMC];
		}
		else return null;
	}
}
