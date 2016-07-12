package uk.ac.glasgow.redeuce.memory;

import java.util.BitSet;

public class DataWord extends Word{
	
	public int[] value;          //Still unsure how data was read in, but we could probably make it work with something like this?
	
//	public DataWord(BitSet binaryDigits) throws Exception {
//		int prevalue = toDecimal(binaryDigits.get(14, 24));
//		value = toBackBinary(prevalue);
//	}
	
	public int[] toBackBinary(int cardValue){
		String binary = String.format("%32s", Integer.toBinaryString(cardValue)).replace(" ", "0");
		int [] binaryValue = new int[32];
		//DEUCE used backwards Binary; hopefully this works
		for (int i=0; i<binary.length(); i++){
			int digit = Character.getNumericValue(binary.charAt(i));
			binaryValue[31 - i] = digit;
		}
		return binaryValue;
	}
	
	public int[] getValue(BitSet binaryDigits){
		int prevalue = toDecimal(binaryDigits.get(14, 24));
		value = toBackBinary(prevalue);
		return value;
	}
}
