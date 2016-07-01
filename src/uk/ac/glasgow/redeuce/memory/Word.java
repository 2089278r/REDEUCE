package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class Word {
	int[] binaryDigits = new int[32];
	boolean isInstruction;
	
	public Word(){
		Arrays.fill(binaryDigits, 0);
		isInstruction=true;
	}
	public Word(int[] binaryDigits){
		this.binaryDigits = binaryDigits;
	}
	
	
}
