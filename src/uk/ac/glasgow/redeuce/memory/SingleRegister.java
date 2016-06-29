package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class SingleRegister extends Register {
	
	public SingleRegister(){
		numberOfWords = 1;
		contents = new String[numberOfWords];
		Arrays.fill(contents, "00000000000000000000000000000000");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
