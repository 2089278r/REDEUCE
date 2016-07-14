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
	
	public Word(int calculatedNumber){
		this.binaryDigits = new BitSet(32);
		String bits = Integer.toBinaryString(calculatedNumber);
		bits = new StringBuilder(bits).reverse().toString();
		char[] bitCharacters = bits.toCharArray();
		for (int i=0; i<bitCharacters.length; i++){
			if (bitCharacters[i] == '1'){
				this.binaryDigits.set(i);	
			}
		}
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
	public int getAsInt(){
		return toDecimal(this.getBits());
	}
	public String toString(){
		String output = "";
		int leadingZeroes = 32 - this.binaryDigits.length();
		for (int i=0; i<this.binaryDigits.length(); i++){
			if (binaryDigits.get(i)){
				output += "1";
			}
			else output += "0";
		}
		for (int i=0; i<leadingZeroes; i++){
			output += "0";
		}
		return output;
	}
	public BitSet getBinaryDigits(){
		return this.binaryDigits;
	}
}
