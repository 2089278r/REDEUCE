package uk.ac.glasgow.redeuce.memory;

public abstract class Word {
	int[] binaryDigits = new int[32];
	
	public Word(){
		for (int i=0; i<32; i++){
			binaryDigits[i] = 0;
		}
	}
	public Word(int[] binaryDigits){
		for (int i=0; i<32; i++){
			this.binaryDigits[i] = binaryDigits[i];
		}
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
