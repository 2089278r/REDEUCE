package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class DoubleRegister extends Register{

	public DoubleRegister(){
		numberOfWords = 2;
		contents = new Word[numberOfWords];
		Arrays.fill(contents, new Word());
	}
	public static void main(Word[] args) {
		// TODO Auto-generated method stub

	}

}
