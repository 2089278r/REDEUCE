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
	
	public Word(int nis, int source, int dest, int characteristic, int wait, int timing, int go){
		this.binaryDigits = new BitSet(32);
		if (nis <8){
			String bits = Integer.toBinaryString(nis);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(1 + i);
				}
			}
		}
		if (source < 32){
			String bits = Integer.toBinaryString(source);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(4 + i);
				}
			}
		}
		if (dest < 32){
			String bits = Integer.toBinaryString(dest);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(9 + i);
				}
			}
		}
		if (characteristic < 3){
			String bits = Integer.toBinaryString(characteristic);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(14 + i);
				}
			}
		}
		if (wait < 32){
			String bits = Integer.toBinaryString(wait);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(16 + i);
				}
			}
		}
		if (timing < 32){
			String bits = Integer.toBinaryString(timing);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(25 + i);
				}
			}
		}
		if (go < 2){
			String bits = Integer.toBinaryString(go);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					this.binaryDigits.set(31 + i);
				}
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
}
