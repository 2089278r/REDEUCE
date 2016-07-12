package uk.ac.glasgow.redeuce.memory;

import java.util.BitSet;

public class Word {
	private BitSet binaryDigits;
	
	public Word(){
		this.binaryDigits = new BitSet(32);
	}
	public Word(BitSet bits){
		this.binaryDigits = bits;
	}

	public int toDecimal(BitSet bits){
		int decimal = 0;
		int currentNumber;
		for (int i=0; i < bits.length(); i++){
			boolean currentBit = bits.get(i);
			if (currentBit == true){
				currentNumber = 1;
			}
			else currentNumber = 0;
			decimal += currentNumber*java.lang.Math.pow(2, i);
		}
		return decimal;
	}
	public int getElements(int from, int to){
		return toDecimal(binaryDigits.get(from, to));
	}
	public BitSet getBits(){
		return this.binaryDigits.get(0, 32);
	}
}
