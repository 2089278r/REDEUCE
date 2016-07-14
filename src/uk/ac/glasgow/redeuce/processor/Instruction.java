package uk.ac.glasgow.redeuce.processor;

import java.util.BitSet;

import uk.ac.glasgow.redeuce.DeuceConstants;
import uk.ac.glasgow.redeuce.memory.Word;

public class Instruction {
	
	Word word;
	int currentPlace;
	public static enum instructionType {TRANSFER, ARITHMETIC, DISCRIM, IO, OTHER}; //Took out constant, as the type of instruction is to do with the Destination really
																				   //The special Sources really only change the word which you're actually manipulating
	private instructionType type;
	
	public Instruction(Word word){
		this.word = word;
		this.type = computeType();
	}
	
	public Instruction(int nis, int source, int dest, int characteristic, int wait, int timing, int go){
		Word makingWord = new Word(new BitSet(32));
		if (nis <8){
			String bits = Integer.toBinaryString(nis);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(1 + i);
				}
			}
		}
		if (source < 32){
			String bits = Integer.toBinaryString(source);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(4 + i);
				}
			}
		}
		if (dest < 32){
			String bits = Integer.toBinaryString(dest);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(9 + i);
				}
			}
		}
		if (characteristic < 3){
			String bits = Integer.toBinaryString(characteristic);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(14 + i);
				}
			}
		}
		if (wait < 32){
			String bits = Integer.toBinaryString(wait);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(16 + i);
				}
			}
		}
		if (timing < 32){
			String bits = Integer.toBinaryString(timing);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(25 + i);
				}
			}
		}
		if (go < 2){
			String bits = Integer.toBinaryString(go);
			bits = new StringBuilder(bits).reverse().toString();
			char[] bitCharacters = bits.toCharArray();
			for (int i=0; i<bits.length(); i++){
				if (bitCharacters[i] == '1'){
					makingWord.getBinaryDigits().set(31 + i);
				}
			}
		}
		this.word = makingWord;
		this.type = computeType();
	}
	
	private instructionType computeType(){
		if (getDest() <= DeuceConstants.MAX_TRANSFER_ADDRESS){
			return instructionType.TRANSFER;
		}
		else if ((getDest() == DeuceConstants.DEST_DISCRIM_SIGN) || (getDest() == DeuceConstants.DEST_DISCRIM_ZERO)){
			return instructionType.DISCRIM;
		}
		else if ((getDest() == DeuceConstants.DEST_DOUBLE_ADD) || 
			(getDest() == DeuceConstants.DEST_DOUBLE_SUB) || 
			(getDest() == DeuceConstants.DEST_SINGLE_ADD) || 
			(getDest() == DeuceConstants.DEST_SINGLE_SUB)){
				return instructionType.ARITHMETIC;
			}
		else if ((getDest() == DeuceConstants.DEST_INPUT_OUTPUT) ||
			(getDest() == DeuceConstants.DEST_PUNCHOUT) ||
			(getDest() == DeuceConstants.DEST_HEADS_MOVE) ||
			(getDest() == DeuceConstants.DEST_INPUT_OUTPUT)){
				return instructionType.IO;
			}
		else {
			return instructionType.OTHER;
		}	
	}
		
	public int getNIS(){
		return word.getElements(1, 4);
	}
	public int getSource(){
		return word.getElements(4, 9);
	}
	public int getDest(){
		return word.getElements(9, 14);
	}
	public int getChar(){
		return word.getElements(14, 16);
	}
	public int getWait(){
		return word.getElements(16, 21);
	}
	public int getTiming(){
		return word.getElements(25, 30);
	}
	public int getGo(){
		return word.getElements(31, 32);
	}
	
	public instructionType getType(){
		return this.type;
	}
	
	public String toString(){
		String output = "Next instruction is at delay line: " + getNIS() + "\n"
						+ "Source Word: " + getSource() + "\n"
						+ "Destination: " + getDest() + "\n"
						+ "Characteristic: " + getChar() + "\n"
						+ "Waiting Time: " + getWait() + "\n"
						+ "Timing: " + getTiming() + "\n"
						+ "And the Go digit is: " + getGo();
		return output;
	}
}
