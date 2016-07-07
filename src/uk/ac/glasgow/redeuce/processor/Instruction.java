package uk.ac.glasgow.redeuce.processor;

import uk.ac.glasgow.redeuce.memory.Word;

public class Instruction {
	
	Word word;
	
	public Instruction(Word word){
		this.word = word;
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
}
