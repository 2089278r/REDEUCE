package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class SingleRegister extends Register {
	
	public SingleRegister(){
		numberOfWords = 1;
		contents = new Word[numberOfWords];
		Arrays.fill(contents, new Word());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
