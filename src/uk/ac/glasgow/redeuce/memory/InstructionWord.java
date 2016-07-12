package uk.ac.glasgow.redeuce.memory;

import java.util.BitSet;

public class InstructionWord extends Word {
	
	public InstructionWord(BitSet binaryDigits) throws Exception{
		super(binaryDigits);
	}
	public InstructionWord(){
		super();
	}
	public int getNIS(){
		return (getElements(1, 4));
	}
	public int getSource(){
		return (getElements(4, 9));
	}
	public int getDest(){
		return (getElements(9, 14));
	}
	public int getChar(){
		return (getElements(14, 16));
	}
	public int getWait(){
		return (getElements(16, 21));
	}
	public int getTiming(){
		return (getElements(25, 30));
	}
	public int getGo(){
		return (getElements(31, 32));
	}
	
}

