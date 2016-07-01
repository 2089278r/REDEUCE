package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class Word {
	int[] binaryDigits = new int[32];
	boolean isInstruction;            //Might not be necessary, depends on how card reader is implemented..
	int dest;
	int timing;
	
	public Word(){
		Arrays.fill(binaryDigits, 0);
		isInstruction=true;
	}
	public Word(int[] binaryDigits){
		this.binaryDigits = binaryDigits;
	}
	public int toDecimal(int[] wordSection){
		int decimal = 0;
		for (int i=0; i < wordSection.length; i++){
			int currentNumber = wordSection[i];
			decimal += currentNumber*java.lang.Math.pow(2, i);
		}
		return decimal;
	}
}
