package uk.ac.glasgow.redeuce.processor;

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
