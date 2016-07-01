package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class InstructionWord extends Word {
	int nis;
	int source;
	int dest;
	int characteristic;
	int wait;
	int timing;
	int go;
	
	public InstructionWord(int[] binaryDigits) throws Exception{
		for (int i=0; i<32; i++){
			if (binaryDigits[i] != 1 && binaryDigits[i] != 0){
				throw new Exception("incorrect format!");
			}
		}
		nis = toDecimal(Arrays.copyOfRange(binaryDigits, 1, 4));
		source = toDecimal(Arrays.copyOfRange(binaryDigits, 4, 9));
		dest = toDecimal(Arrays.copyOfRange(binaryDigits, 9, 14));
		characteristic = toDecimal(Arrays.copyOfRange(binaryDigits, 14, 16));
		wait = toDecimal(Arrays.copyOfRange(binaryDigits, 16, 21));
		timing = toDecimal(Arrays.copyOfRange(binaryDigits, 25, 30));
		go = toDecimal(Arrays.copyOfRange(binaryDigits, 31, 32));
	}
	
	//private int toDecimal(int[] wordSection){
	//	int decimal = 0;
	//	for (int i=0; i < wordSection.length; i++){
	//		int currentNumber = wordSection[i];
	//		decimal += currentNumber*java.lang.Math.pow(2, i);
	//	}
	//	return decimal;
	//}
	
}

