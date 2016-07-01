package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class InstructionWord extends Word {
	
	public InstructionWord(int[] binaryDigits) throws Exception{
		super(binaryDigits);
		for (int i=0; i<32; i++){
			if (binaryDigits[i] != 1 && binaryDigits[i] != 0){
				throw new Exception("incorrect format!");
			}
		}
	}
	public int getNIS(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 1, 4));
	}
	public int getSource(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 4, 9));
	}
	public int getDest(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 9, 14));
	}
	public int getChar(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 14, 16));
	}
	public int getWait(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 16, 21));
	}
	public int getTiming(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 25, 30));
	}
	public int getGo(){
		return toDecimal(Arrays.copyOfRange(binaryDigits, 31, 32));
	}
	
}

